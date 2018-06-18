package com.scent.feedservice.steps.poststeps;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.data.feed.PrivacyType;
import com.scent.feedservice.repositories.PostRepository;
import com.scent.feedservice.steps.IAction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.POST_ID;
import static com.scent.feedservice.Util.Constants.PRIVACY_TYPE;

@Component
public class ChangePrivacyStep implements IAction {

    private PostRepository postRepository;
    public ChangePrivacyStep(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        String postId = paramMap.get(POST_ID);
        String privacy = paramMap.get(PRIVACY_TYPE);

        postRepository.getPostByPostId(postId).flatMap(
          post -> {
              post.setPrivacy(PrivacyType.valueOf(privacy));
              return Mono.just(post);
          }).subscribe(this::updatePost);
    }

    private void updatePost(Post post){
        postRepository.save(post).subscribe();
    }
}
