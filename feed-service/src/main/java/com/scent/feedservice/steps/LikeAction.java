package com.scent.feedservice.steps;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Like;
import com.scent.feedservice.repositories.LikeRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static com.scent.feedservice.Util.Constants.POST_ID;
import static com.scent.feedservice.Util.Constants.USER_ID;

@Component
public class LikeAction implements IAction {
    private LikeRepository likeRepository;
    public LikeAction(LikeRepository likeRepository){
        this.likeRepository = likeRepository;
    }
    @Override
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        //Create like entry
        Mono<Like> likeMono = likeRepository.getLikeByPostId(paramMap.get(POST_ID));

        likeMono.flatMap(like -> {

            if(like == null){
                return Mono.just(like);
            }else{
                LongStream.range(0, 900000).mapToObj(i -> i + "").forEach(like::addUser);
            }
           return Mono.just(like);
        }).subscribe(this::createLike, this::errorHandler);



    }
    private void createLike(Like like){
        likeRepository.save(like).subscribe(System.out::println);
    }
    private void errorHandler(Throwable ex){
        System.out.print(ex.toString());
    }
//    private void createLikeEntry(Like flag, Map<String, String> paramMap ){
//      if(flag==null) {
//
//          Like like = new Like();
//          like.setLikeId(UUID.randomUUID().toString());
//          like.setPostId(paramMap.get(POST_ID));
//          like.setPostId(paramMap.get(USER_ID));
//          likeRepository.save(like).subscribe(System.out::println);
//      }
//
//    }




}
