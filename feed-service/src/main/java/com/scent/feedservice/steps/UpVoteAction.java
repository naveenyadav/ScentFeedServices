package com.scent.feedservice.steps;

import com.scent.feedservice.Util.CommonUtil;
import com.scent.feedservice.Util.ConfigServiceImpl;
import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Like;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.repositories.LikeRepository;
import com.scent.feedservice.repositories.PostRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import reactor.core.publisher.SignalType;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.logging.Level;

import static com.scent.feedservice.Util.Constants.*;

@Component
public class UpVoteAction implements IAction {
    private Logger LOG = Loggers.getLogger(UpVoteAction.class);

    @Autowired
    protected ConfigServiceImpl configServiceImpl;

    private LikeRepository likeRepository;

    private PostRepository postRepository;

    public UpVoteAction(PostRepository postRepository, LikeRepository likeRepository){
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }


    @Override
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        //Get Year also
        String year = paramMap.get(YEAR);
        String userId = paramMap.get(USER_ID);
        String likeUserId = year.concat(UNDER_SCORE).concat(userId);
        String postId =  paramMap.get(POST_ID);

        //Get like by UserID
        Mono<Long> likeMono = likeRepository.countLikesByUserId(likeUserId)
                .single().tag("GetLike", "getLike").log(LOG, Level.INFO, true, SignalType.CURRENT_CONTEXT);

        //Get Post By postId
        Mono<Post> postMono = postRepository.getPostByPostId(postId).tag("GetPost", "getPost")
                .flatMap(post -> processPost(post, userId)).tag("UpVote", "upVote").log(LOG, Level.INFO, true, SignalType.CURRENT_CONTEXT);

        likeMono.zipWith(postMono).log().subscribe(tuple -> onSuccess(tuple,  paramMap));
        //Save like
        //update post
    }


    public void onSuccess(Tuple2<Long, Post> tuple, Map<String, String> paramMap){

        Long likeCout = tuple.getT1();

        Post post = tuple.getT2();
        String year = paramMap.get(YEAR);
        String userId = paramMap.get(USER_ID);
        String likeUserId = year.concat(UNDER_SCORE).concat(userId);
        String postId = paramMap.get(POST_ID);
        Mono<Like> likeMono = Mono.empty();
        if(likeCout == 0 && post != null) {
            Like like = new Like();
            like.setUserId(likeUserId);
            like.addPosts(postId);
            //Save like
            likeMono = likeRepository.save(like);
        }else if(likeCout == 1 && null != post){
            //only add postId in like database
            likeMono = likeRepository.getLikeByUserId(likeUserId).flatMap( like -> {
                if(like.addPosts(postId)) {
                    return likeRepository.save(like);
                }
                return Mono.empty();
            });
        }
        likeMono.subscribe(like -> savePost(like, post));
    }
    private void savePost(Like like, Post post){

        if(like != null)
          postRepository.save(post).subscribe();
    }

    public Mono<Post> processPost(Post post, String userId){
        boolean result = !post.getUserId().equals(userId);
        if(result) {
            // add one vote
            post.setVotes(post.getVotes() + 1);

            //Get post node expiry date
            String expiryDate = post.getExpiryDate();

            //Get specified hour configured for various upvotes level
            int upVoteHour = configServiceImpl.getPropertyValueAsInteger(GLOBAL_CONFIG, upVoteIncrement(post));

            //Add the upVote hour to expiry date
            String date = DateUtil.updateHourToExpiryDate(expiryDate, upVoteHour, POST_TIME_PATTERN, TIMEZONE_UTC);

            //now change the expiry date
            post.setExpiryDate(date);
            return Mono.just(post);
        }else{
            return Mono.empty();
        }
    }


    public void onSavingSucceess(Post post, EventData eventData){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", post);
        updateResponse("UpVote", eventData, jsonObject);
    }

    public void onError(Throwable throwable, EventData eventData){
        JSONObject jsonObject = CommonUtil.getErrorResponse(throwable.toString(), EMPTY, true);
        updateResponse("UpVote", eventData, jsonObject);
    }

    private String upVoteIncrement(Post post){
        if(post.getVotes() >= 2000)
            return PROP_POST_HOUR_UP_VOTE_2000;
        else if(post.getVotes() >= 1000 && post.getVotes() < 1000){
            return PROP_POST_HOUR_UP_VOTE_1000;
        }
        return PROP_POST_HOUR_UP_VOTE;
    }
}
