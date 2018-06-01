package com.scent.feedservice.repositories;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.feed.Post;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;

import static com.scent.feedservice.Util.Constants.POST_ID;
import static com.scent.feedservice.Util.Constants.USER_ID;

public class GetCommentbyPostID extends AbstractRepository{

//    private PostRepository postRepository;
//    public GetCommentbyPostID(PostRepository postRepository){
//        this.postRepository = postRepository;
//    }
//    @PostConstruct
//    public void init() {
//        String[] requiredFields = new String[]{POST_ID, USER_ID};
//        addToRequiredFieldList(requiredFields);
//    }
//
//    public ResponseData handleEvent(String eventName, EventData eventData){
//        final RequestData requestData = eventData.getRequestData();
//        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
//        Mono<Post> post = postRepository.getPostByPostId(paramMap.get(POST_ID));
//
//    }
}
