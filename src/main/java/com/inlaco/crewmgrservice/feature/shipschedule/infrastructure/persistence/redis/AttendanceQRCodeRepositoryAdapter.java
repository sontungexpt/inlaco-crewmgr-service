package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.redis;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceQRCodeRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceQRCodeRepositoryAdapter implements AttendanceQRCodeRepository {

  private static final String KEY_PREFIX = "attendance:qr:";

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public AttendanceQRCode save(AttendanceQRCode qrCode) {

    String key = buildKey(qrCode.getToken());

    Duration ttl = Duration.between(qrCode.getCreatedAt(), qrCode.getExpiresAt());

    redisTemplate.opsForValue().set(key, qrCode, ttl);

    return qrCode;
  }

  @Override
  public Optional<AttendanceQRCode> findByToken(String token) {
    Object value = redisTemplate.opsForValue().get(buildKey(token));
    if (value == null) {
      return Optional.empty();
    }

    return Optional.of((AttendanceQRCode) value);
  }

  @Override
  public void deleteByToken(String token) {
    redisTemplate.delete(buildKey(token));
  }

  private String buildKey(String token) {
    return KEY_PREFIX + token;
  }
}
