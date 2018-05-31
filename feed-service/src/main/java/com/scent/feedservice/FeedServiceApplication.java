package com.scent.feedservice;

import com.mongodb.MongoClientURI;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.scent.feedservice.Util.ConfigServiceImpl;
import com.scent.feedservice.data.Employee;
import com.scent.feedservice.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.core.env.Environment;

import java.util.UUID;

import static com.scent.feedservice.Util.Constants.*;

@SpringBootApplication
public class FeedServiceApplication extends AbstractReactiveMongoConfiguration {

    @Autowired
    private ConfigServiceImpl configServiceImpl;


    private final Environment environment;

    public FeedServiceApplication(Environment environment) {
        this.environment = environment;
    }
    public static void main(String[] args) {
        SpringApplication.run(FeedServiceApplication.class, args);

    }
    @Override
    @Bean
    public MongoClient reactiveMongoClient() {
        String url = configServiceImpl.getPropertyValueAsString(GLOBAL_CONFIG, MONGO_DB_PATH);
        MongoClientURI uri = new MongoClientURI(url);
        System.out.println(uri.getURI());
        return MongoClients.create(uri.getURI());
    }

    @Override
    protected String getDatabaseName() {
        return configServiceImpl.getPropertyValueAsString(GLOBAL_CONFIG, MONGO_DB_NAME);
    }




}
