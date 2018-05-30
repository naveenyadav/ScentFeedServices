package com.scent.feedservices.repositories;

import com.scent.feedservices.data.Comment;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

}
