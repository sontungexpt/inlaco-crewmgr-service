// package com.inlaco.crewmgrservice.feature.contract.timertask;

// import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
// import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
// import static org.springframework.data.mongodb.core.query.Criteria.where;

// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.aggregation.Aggregation;
// import org.springframework.data.mongodb.core.aggregation.DateOperators;
// import org.springframework.data.mongodb.core.aggregation.DateOperators.TemporalUnit;
// import org.springframework.data.mongodb.core.query.Criteria;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;

// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class ContractFreezeSchedule {

//   private final MongoTemplate mongoTemplate;

//   /**
//    * Get all the contracts which are signed and freeze them.after frezze delay mintues Run every
//    * minute
//    */
//   @Scheduled(cron = "0 0/1 * * * ?")
//   public void frezzeConrtact() {
//     Aggregation aggregation =
//         Aggregation.newAggregation(
//             project()
//                 .and(
//                     DateOperators.dateOf("signedAt")
//                         .addValueOf("contractFreezeDelay",
// TemporalUnit.from(ChronoUnit.MINUTES)))
//                 .as("freezeDate"),
//             match(where("signed").is(false).and("freezeDate").gte(Instant.now())));,
//             Aggregation.addFields().addFieldWithValue("status", Comic.Status.ONGOING).build(),
//             Aggregation.merge().intoCollection("comics").build());
//   }
// }
