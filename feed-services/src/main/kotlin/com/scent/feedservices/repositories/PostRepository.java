package com.scent.feedservices.repositories;

import com.scent.feedservices.data.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Mono<Post> findByPostId(String postId);
    Flux<Post> findDistinctTopBy

}
