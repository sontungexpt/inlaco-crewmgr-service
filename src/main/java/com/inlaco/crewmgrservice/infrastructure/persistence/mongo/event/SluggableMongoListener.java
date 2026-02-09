package com.inlaco.crewmgrservice.infrastructure.persistence.mongo.event;
// package com.inlaco.crewmgrservice.infrastructure.mongo.event;

// import com.inlaco.crewmgrservice.domain.model.Sluggable;
// import com.inlaco.crewmgrservice.domain.service.SlugGenerator;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
// import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
// import org.springframework.stereotype.Component;

// @Component
// @RequiredArgsConstructor
// public class SluggableMongoListener extends AbstractMongoEventListener<Sluggable<?>> {

//   private final MongoTemplate mongoTemplate;
//   private final SlugGenerator slugGenerator;

//   @Override
//   public void onBeforeConvert(BeforeConvertEvent<Sluggable<?>> event) {
//     Sluggable<?> entity = event.getSource();
//     Object oldEntity =
//         entity.getId() == null ? null : mongoTemplate.findById(entity.getId(),
// entity.getClass());
//     slugGenerator.generate(entity, oldEntity);
//   }
// }
