package com.inlaco.crewmgrservice.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.adapter.CrewProfileRepositoryAdapter;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.mapper.CrewProfileEntityMapper;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.repository.CrewProfileMongoRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = {"spring.mongodb.uri=mongodb://localhost:27017/test"})
@Import({CrewProfileRepositoryAdapter.class, CrewProfileEntityMapper.class})
class CrewProfileRepositoryAdapterTest {
  @Autowired CrewProfileRepositoryAdapter adapter;

  @Autowired CrewProfileMongoRepository repository;

  @Test
  void should_bulk_insert_and_update() {

    // Given
    CrewProfile newProfile =
        CrewProfile.builder()
            .fullName("John")
            .email("4gM9W@example.com")
            .phoneNumber("1234567890")
            .address("123 Main St")
            .build();

    CrewProfile saved = adapter.saveAll(List.of(newProfile)).get(0);

    assertNotNull(saved.getId());

    // Update
    saved.setFullName("John Updated");

    List<CrewProfile> result = adapter.saveAll(List.of(saved));

    CrewProfile updated = result.get(0);

    assertEquals("John Updated", updated.getFullName());
  }

  @Test
  void should_handle_large_batch() {

    List<CrewProfile> profiles =
        IntStream.range(0, 1000)
            .mapToObj(i -> CrewProfile.builder().fullName("User " + i).build())
            .toList();

    long start = System.currentTimeMillis();

    adapter.saveAll(profiles);

    long end = System.currentTimeMillis();

    System.out.println("Time: " + (end - start));
  }
}
