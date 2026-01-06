package com.inlaco.crewmgrservice.feature.contract;

import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractVersionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ContractRepositoryTest {

  @Autowired private ContractRepository contractRepository;

  @Autowired private ContractVersionRepository contractVersionRepository;

  @Test
  public void testCreateContractVersion() {

    var contract = LaborContract.builder().title("test").build();
    contractVersionRepository.save(contract);
  }

  @Test
  public void testCreateContract() {
    var contract = LaborContract.builder().title("test").build();
    contractRepository.save(contract);
  }
}
