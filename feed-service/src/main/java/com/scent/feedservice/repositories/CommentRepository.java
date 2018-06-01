package com.scent.feedservice.repositories;

import com.scent.feedservice.data.feed.Comment;
import com.scent.feedservice.data.feed.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Mono<Comment> getCommentByCommentIdAndAndOwnerUserId(String commentId, String userId);

}
