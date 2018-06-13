package com.scent.feedservice.repositories;


import com.scent.feedservice.data.feed.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Mono<Post> getPostByPostId(String postId);
    Mono<Boolean> getPostByPostIdExists(String postId);


}
