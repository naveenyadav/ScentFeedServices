package com.scent.feedservice.repositories;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.feed.Post;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;

public class GetPostByID extends AbstractRepository{
    private PostRepository postRepository;
    public GetPostByID(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    @PostConstruct
    public void init() {
        String[] requiredFields = new String[]{POST_ID, USER_ID};
        addToRequiredFieldList(requiredFields);
    }

    public ResponseData handleEvent(String eventName, EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        Mono<Post> post = postRepository.getPostByPostId(paramMap.get(POST_ID));

    }
}
