package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractQueryUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractSnapshotRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractQueryService implements ContractQueryUseCase {

  private final ContractSnapshotRepository contractSnapshotRepository;
  private final ContractRepository contractRepository;

  @Override
  public Contract getContract(String id, @Nullable Integer version, User user) {
    Contract contract;
    if (version == null || version < 1) {
      contract =
          contractRepository
              .findById(id)
              .orElseThrow(() -> new ResourceNotFoundException(Contract.class, "id", id));
    }

    contract =
        contractSnapshotRepository
            .findByContractIdAndVersion(id, version)
            .orElseGet(
                () ->
                    contractRepository
                        .findById(id)
                        .orElseThrow(
                            () -> new ResourceNotFoundException(Contract.class, "id", id)));

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return contract;
    }
    Party partner = contract.getPartners().get(0);
    if (partner == null) {
      throw new ResourceNotFoundException(Party.class, "id", id);
    } else if (!partner.getAccountId().equals(user.getId())) {
      throw new ResourceNotFoundException(Contract.class, "id", id);
    }

    return contract;
  }

  @Override
  public Page<? extends Contract> getContracts(ContractSearchCriteria criteria, Pageable pageable) {
    return contractRepository.findAll(criteria, pageable);
  }

  @Override
  public List<Contract> getOldContractVersions(String contractId) {
    return contractSnapshotRepository.findByContractId(contractId).stream()
        .sorted(Comparator.comparing(Contract::getVersion))
        .toList();
  }

  @Override
  public Page<? extends Contract> getContractsByUser(
      ContractSearchCriteria criteria, User user, Pageable pageable) {
    criteria = criteria != null ? criteria : new ContractSearchCriteria();
    criteria.setRelativeAccountId(user.getId());
    return contractRepository.findAll(criteria, pageable);
  }
}
