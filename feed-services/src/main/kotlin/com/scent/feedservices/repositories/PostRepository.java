package com.scent.feedservices.repositories;

import com.scent.feedservices.data.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {

}
