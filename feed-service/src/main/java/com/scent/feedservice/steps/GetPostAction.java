package com.scent.feedservice.steps;

import com.scent.feedservice.Util.CommonUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.repositories.PostRepository;
import org.json.JSONObject;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.EMPTY;
import static com.scent.feedservice.Util.Constants.POST_ID;

public class GetPostAction implements IAction {
    private PostRepository postRepository;

    public GetPostAction(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    @Override
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
       postRepository.getPostByPostId(paramMap.get(POST_ID)).single().subscribe( (post->
            onSavingSucceess(post, eventData)), error -> this.onError(error, eventData));
    }
    public void onSavingSucceess(Post post, EventData eventData){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", post);
        updateResponse("GetPost", eventData, jsonObject);
    }
    public void onError(Throwable throwable, EventData eventData){
        JSONObject jsonObject = CommonUtil.getErrorResponse(throwable.toString(), EMPTY, true);
        updateResponse("GetPost", eventData, jsonObject);
    }
}
