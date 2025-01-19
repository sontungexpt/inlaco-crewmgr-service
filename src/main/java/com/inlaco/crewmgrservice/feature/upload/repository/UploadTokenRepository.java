package com.inlaco.crewmgrservice.feature.upload.repository;

import java.util.Optional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class UploadTokenRepository {

  private final RedisTemplate<Object, Object> redisTemplate;
  private final ValueOperations<Object, Object> valueOperations;

  private final String KEY_PREFIX = "upload-token:";

  public UploadTokenRepository(RedisTemplate<Object, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.valueOperations = redisTemplate.opsForValue();
  }

  private String getKey(String token) {
    return KEY_PREFIX + token;
  }

  public void save(String token, Object id) {
    valueOperations.set(getKey(token), id);
  }

  public void delete(String token) {
    redisTemplate.delete(getKey(token));
  }

  public boolean exists(String token) {
    return redisTemplate.hasKey(getKey(token));
  }

  public Optional<Object> findByToken(String token) {
    return Optional.ofNullable(valueOperations.get(getKey(token)));
  }
}
