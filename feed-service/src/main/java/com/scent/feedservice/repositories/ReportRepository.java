package com.scent.feedservice.repositories;

import com.scent.feedservice.data.feed.Like;
import com.scent.feedservice.data.profile.Report;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReportRepository extends ReactiveMongoRepository<Report, String> {
    Mono<Report> getReportByUserId(String userId);
}
