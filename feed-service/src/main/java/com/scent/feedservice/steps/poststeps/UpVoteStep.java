package com.scent.feedservice.steps.poststeps;

import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Like;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.repositories.LikeRepository;
import com.scent.feedservice.repositories.PostRepository;
import com.scent.feedservice.steps.IAction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import reactor.core.publisher.SignalType;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.function.Tuple2;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

import static com.scent.feedservice.Util.Constants.*;

@Component
public class UpVoteStep implements IAction {
    private Logger LOG = Loggers.getLogger(UpVoteStep.class);


    private LikeRepository likeRepository;

    private PostRepository postRepository;

    public UpVoteStep(PostRepository postRepository, LikeRepository likeRepository){
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }


    @Override
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        //Get Year also
        String userId = paramMap.get(USER_ID);
        String postId =  paramMap.get(POST_ID);

        //Get like by UserID
        Mono<Long> likeMono = likeRepository.countLikesByUserId(userId)
                .single().tag("GetLike", "getLike").log(LOG, Level.INFO, true, SignalType.CURRENT_CONTEXT);

        //Get Post By postId
        Mono<Post> postMono = postRepository.getPostByPostId(postId);

        likeMono.zipWith(postMono).flatMap(tuple -> onSuccess(tuple, paramMap)).subscribe();
    }


    private Mono<Tuple2<Like, Post>> onSuccess(Tuple2<Long, Post> tuple, Map<String, String> paramMap){
        Long likeCount = tuple.getT1();
        Post post = tuple.getT2();
        String userId = paramMap.get(USER_ID);
        String postId = paramMap.get(POST_ID);
        String vote = paramMap.get(VOTE);
        Mono<Like> likeMono = Mono.empty();
        paramMap.put(PARAM_VOTE, String.valueOf(ZERO));
        boolean result = !post.getUserId().equals(userId);
        if(likeCount == 0 && post != null && result) {
            //Like for the user like row is not created
            Like like = new Like();
            like.setUserId(userId);
            if(vote.equals(UP_VOTE)) {
                paramMap.put(PARAM_VOTE, String.valueOf(ONE));
                like.addUpVotes(postId);
            }else{
                paramMap.put(PARAM_VOTE, String.valueOf(MINUS_ONE));
                like.addDownVotes(postId);
            }
            //Save like
            likeMono = likeRepository.save(like);

        }else if(likeCount == 1 && null != post && result){
            //only add postId in like database
            likeMono = likeRepository.getLikeByUserId(userId).flatMap( like -> {
                if(vote.equals(UP_VOTE)) {
                    if (like.removeDownVotes(postId)) {
                        //Already downVoted Now wants to upVote
                        paramMap.put(PARAM_VOTE, String.valueOf(TWO));
                    } else {
                        //first time upVoting the post
                        paramMap.put(PARAM_VOTE, String.valueOf(ONE));
                    }
                    like.addUpVotes(postId);
                }else if(vote.equals(DOWN_VOTE)){
                    if(like.removeUpVotes(postId)){
                        //Already upVoted Now wants to downVote
                        paramMap.put(PARAM_VOTE, String.valueOf(MINUS_TWO));
                    }else{
                        //first time downVoting the post
                        paramMap.put(PARAM_VOTE, String.valueOf(MINUS_ONE));
                    }
                    like.addDownVotes(postId);
                }
                return likeRepository.save(like);
            });
        }
        int counter = Integer.parseInt(paramMap.get(PARAM_VOTE));
        post = processPost(post, counter, result);
        Mono<Post> postMono = postRepository.save(post);
        return likeMono.zipWith(postMono);
    }

    public Post processPost(Post post, int counter, Boolean result){
        if(result) {
            // add vote
            post.setVotes(post.getVotes() + counter);

            //Add the upVote hour to expiry date
            Date updatedDate = DateUtil.updateHourToExpiryDate(post.getExpiryDate(), counter, TIMEZONE_UTC);

            //now change the expiry date
            post.setExpiryDate(updatedDate);
        }
        return post;
    }
}
