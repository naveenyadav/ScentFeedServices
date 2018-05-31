package com.scent.feedservices.repositories;

import com.scent.feedservices.data.Post;
import com.scent.feedservices.data.Test;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveMongoRepository<Test, String> {

}
