package com.scent.feedservice.repositories;


import com.scent.feedservice.data.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CommentRepository extends ReactiveMongoRepository<Post, String> {
}
