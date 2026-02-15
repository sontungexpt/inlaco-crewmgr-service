package com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.entity.SequenceEntity;
import com.inlaco.crewmgrservice.shared.application.port.out.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoSequenceGenerator implements SequenceGenerator {

  private final MongoTemplate mongoTemplate;

  @Override
  public long next(String key) {
    Query query = new Query(Criteria.where("_id").is(key));

    Update update = new Update().inc("seq", 1);

    FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

    SequenceEntity counter =
        mongoTemplate.findAndModify(query, update, options, SequenceEntity.class);

    return counter.getSeq();
  }
}
