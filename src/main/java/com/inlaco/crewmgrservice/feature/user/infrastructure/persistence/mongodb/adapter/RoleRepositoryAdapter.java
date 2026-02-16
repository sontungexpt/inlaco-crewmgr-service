package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.user.application.port.out.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.Role;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.RoleEntity;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.mapper.RoleEntityMapper;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.repository.RoleMongoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {
  private final RoleMongoRepository repository;
  private final RoleEntityMapper mapper;

  @Override
  public boolean existsByName(String name) {
    return repository.existsByName(name);
  }

  @Override
  public Optional<Role> findByName(String name) {
    return repository.findByName(name).map(mapper::toRole);
  }

  @Override
  @Cacheable(value = "roles", key = "#id", unless = "#result.empty")
  public Optional<Role> findById(String id) {
    return repository.findById(id).map(mapper::toRole);
  }

  @Override
  public List<Role> findAll() {
    return repository.findAll().stream().map(mapper::toRole).toList();
  }

  @Override
  public List<Role> findAllById(Iterable<String> ids) {
    List<Role> roles = new ArrayList<>();
    ids.forEach(id -> findById(id).ifPresent(roles::add));
    return roles;
  }

  @Override
  @CacheEvict(value = "roles", key = "#result.id")
  public Role save(Role role) {
    String id = role.getId();
    if (id == null) {
      return mapper.toRole(repository.insert(mapper.toRoleEntity(role)));
    } else {
      RoleEntity entity =
          repository
              .findById(id)
              .map(
                  existing -> {
                    mapper.updateFromRole(role, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toRoleEntity(role));
      return mapper.toRole(repository.save(entity));
    }
  }
}
