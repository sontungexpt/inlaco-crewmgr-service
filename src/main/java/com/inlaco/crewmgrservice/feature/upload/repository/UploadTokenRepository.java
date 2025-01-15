package com.inlaco.crewmgrservice.feature.upload.repository;

import com.inlaco.crewmgrservice.feature.upload.dto.UploadToken;
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

  private String getKey(UploadToken token) {
    return KEY_PREFIX + token.getToken();
  }

  public void save(UploadToken token, Object id) {
    valueOperations.set(getKey(token), id);
  }

  public void delete(UploadToken token) {
    redisTemplate.delete(getKey(token));
  }

  public boolean exists(UploadToken token) {
    return redisTemplate.hasKey(getKey(token));
  }

  public Object findByToken(UploadToken token) {
    return valueOperations.get(getKey(token));
  }
}
