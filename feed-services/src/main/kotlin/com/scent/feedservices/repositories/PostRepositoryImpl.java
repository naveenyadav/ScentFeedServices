package com.scent.feedservices.repositories;

import com.scent.feedservices.data.Post;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PostRepositoryImpl{
    private PostRepository _postRepository;
    @Autowired
    public PostRepositoryImpl(PostRepository postRepository){
        _postRepository = postRepository;

    }

    @Override
    public <S extends Post> Mono<S> insert(S entity) {
        return null;
    }

    @Override
    public <S extends Post> Flux<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Post> Flux<S> insert(Publisher<S> entities) {
        return null;
    }

    @Override
    public <S extends Post> Mono<S> findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Post> Flux<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Post> Flux<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Post> Mono<Long> count(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Post> Mono<Boolean> exists(Example<S> example) {
        return null;
    }

    @Override
    public Flux<Post> findAll(Sort sort) {
        return null;
    }

    @Override
    public  Mono<Post> save(String postId) {
        Post post = new Post();
        _postRepository.save()
    }

    @Override
    public <S extends Post> Flux<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Post> Flux<S> saveAll(Publisher<S> entityStream) {
        return null;
    }

    @Override
    public Mono<Post> findById(String s) {
        return null;
    }

    @Override
    public Mono<Post> findById(Publisher<String> id) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(String s) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<String> id) {
        return null;
    }

    @Override
    public Flux<Post> findAll() {
        return null;
    }

    @Override
    public Flux<Post> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public Flux<Post> findAllById(Publisher<String> idStream) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String s) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Publisher<String> id) {
        return null;
    }

    @Override
    public Mono<Void> delete(Post entity) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends Post> entities) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends Post> entityStream) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
}
