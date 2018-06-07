package com.scent.feedservice.steps;

import com.scent.feedservice.Util.CommonUtil;
import com.scent.feedservice.Util.ConfigServiceImpl;
import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.repositories.PostRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;

public class UpVoteAction implements IAction {
    @Autowired
    protected ConfigServiceImpl configServiceImpl;

    private PostRepository postRepository;
    public UpVoteAction(PostRepository postRepository){
        this.postRepository = postRepository;
    }


    @Override
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        //Get Post By postId
        Mono<Post> postMono = postRepository.getPostByPostId(paramMap.get(POST_ID));
        JSONObject json =  null;
        if(eventData.getResponseData().getData("GetPost") instanceof JSONObject){
            json =  (JSONObject) eventData.getResponseData().getData("GetPost");
        }
        if(CommonUtil.isSuccessResponse(json)) {
            Post post = (Post) json.get("data");
            boolean result = !post.getUserId().equals(paramMap.get(USER_ID));
            if(result) {
                //Get post node expiry date
                String expiryDate = post.getExpiryDate();
                //Get specified hour configured for various upvotes level
                int upVoteHour = configServiceImpl.getPropertyValueAsInteger(GLOBAL_CONFIG, upVoteIncrement(post));
                //Add the upVote hour to expiry date
                String date = DateUtil.updateHourToExpiryDate(expiryDate, upVoteHour, POST_TIME_PATTERN, TIMEZONE_UTC);
                //now change the expiry date
                post.setExpiryDate(date);
                postRepository.save(post).subscribe(post1 -> onSavingSucceess(post, eventData), error->onError(error, eventData));
            }
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
