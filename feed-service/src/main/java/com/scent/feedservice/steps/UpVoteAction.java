package com.scent.feedservice.steps;

import com.scent.feedservice.Util.ConfigServiceImpl;
import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.repositories.PostRepository;
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
    public void performAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        Mono<Post> postMono = postRepository.getPostByPostId(paramMap.get(POST_ID));
        postMono.flatMap(post -> {
            //Rule 1 - is not owner of the post
            boolean result = !post.getUserId().equals(paramMap.get(USER_ID));
            if(result) {
                String expiryDate = post.getExpiryDate();
                int upVoteHour = configServiceImpl.getPropertyValueAsInteger(GLOBAL_CONFIG, upVoteIncrement(post));
                String date = DateUtil.updateHourToExpiryDate(expiryDate, upVoteHour, POST_TIME_PATTERN, TIMEZONE_UTC);
                post.setExpiryDate(date);
                return Mono.just(post);
            }else{
                return Mono.empty();
            }
        }).flatMap(postRepository::save).subscribe(System.out::println);
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
