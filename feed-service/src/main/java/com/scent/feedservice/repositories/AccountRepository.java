package com.scent.feedservice.repositories;


import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.data.profile.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> getAccountByEmailAndPassword(String email, String password);
    Mono<Long> countAccountByEmail(String email);
    Mono<Long> countAccountByMobileNumber(String mobile);
    Mono<Account> getAccountByMobileNumberAndPassword(String mobile, String password);
    Mono<String> getAccountByEmailExists(String email);

}
