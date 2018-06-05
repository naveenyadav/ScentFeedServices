package com.scent.feedservice.repositories;

import com.scent.feedservice.data.feed.Comment;
import com.scent.feedservice.data.feed.Like;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LikeRepository extends ReactiveMongoRepository<Like, String> {
   Mono<Like> getLikeByUserId(String userId);

   Mono<Long> countLikesByUserId(String userId);
}
