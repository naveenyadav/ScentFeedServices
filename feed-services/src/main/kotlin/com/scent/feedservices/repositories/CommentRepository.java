package com.scent.feedservices.repositories;

import com.scent.feedservices.data.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Post, String> {

}
