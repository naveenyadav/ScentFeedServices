package com.scent.feedservice.repositories;


import com.scent.feedservice.data.feed.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Mono<Post> getPostByPostId(String postId);
    Mono<Void> getPostByPostIdExists(String Id);
}
