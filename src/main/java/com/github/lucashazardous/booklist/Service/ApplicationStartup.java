package com.github.lucashazardous.booklist.Service;

import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup
        implements ApplicationListener<ApplicationReadyEvent> {
    private final MongoTemplate mongoTemplate;

    public ApplicationStartup(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onApplicationEvent(final @NonNull ApplicationReadyEvent event) {
        mongoTemplate.indexOps("book").ensureIndex(
                new Index().on("title", Sort.DEFAULT_DIRECTION).on("author", Sort.DEFAULT_DIRECTION).unique()
        );
    }
}