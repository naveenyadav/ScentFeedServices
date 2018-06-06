package com.scent.feedservice.steps;

import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Like;
import com.scent.feedservice.repositories.LikeRepository;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;

public class SaveLikeAction implements  IAction{
    private LikeRepository likeRepository;

    public SaveLikeAction(LikeRepository likeRepository){
        this.likeRepository = likeRepository;
    }
    @Override
    public void performAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        String postId = paramMap.get(POST_ID);
        String userId = paramMap.get(USER_ID);
        String year = DateUtil.getCurrentYear(paramMap.get(DATE), POST_TIME_PATTERN, TIMEZONE_UTC);
        Like like = new Like();
        like.setUserId(year + "_" + userId);
        like.addPosts(postId);
        likeRepository.save(like).subscribe(this::onSuccess, this::onError);
    }

    public void onSuccess(Like like){

    }
    public void onError(Throwable throwable){

    }
}
