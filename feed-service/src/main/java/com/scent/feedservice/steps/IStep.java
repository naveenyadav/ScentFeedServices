package com.scent.feedservice.steps;

import com.scent.feedservice.data.EventData;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public interface IStep<T> {
    Mono<T> executeStep(EventData eventData);
    default Map<String, String> getRequestParamsCopy(Map<String, String> dataMap) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.putAll(dataMap);
        return paramsMap;
    }
}
