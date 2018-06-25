package com.scent.feedservice.steps.feed;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.data.feed.StatusType;
import com.scent.feedservice.repositories.PostRepository;
import com.scent.feedservice.steps.IAction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.POST_ID;

@Component
public class DeletePostStep implements IAction {

    private PostRepository postRepository;
    public DeletePostStep(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public ResponseData perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        String postId = paramMap.get(POST_ID);

        postRepository.getPostByPostId(postId).flatMap(
                post -> {
                    post.setFlagToDelete(StatusType.DELETE_PENDING);
                    return Mono.just(post);
                }).subscribe(this::updatePost);

        return eventData.getResponseData();

    }

    private void updatePost(Post post){
        postRepository.save(post).subscribe();
    }
}
