package com.scent.feedservice.steps;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class CreateLikeSubscriber<Like> extends BaseSubscriber<Like> {

    public void hookOnSubscribe(Subscription subscription){
        subscription.request(Long.MAX_VALUE);
    }
    public void hookOnNext(Like like){
        System.out.print(like.toString());
    }
    @Override
    public void hookOnError(Throwable throwable){
        System.out.print(throwable.toString());
    }
}

