package com.inlaco.crewmgrservice.feature.contract.service;

import com.inlaco.crewmgrservice.feature.contract.model.Contract;

public interface ContractService {

  /**
   * Gets the contract by the contract ID.
   *
   * @param id The contract ID.
   * @return The contract object.
   */
  Contract getContractById(String id);

  /**
   * Gets the contract by the sailor ID.
   *
   * @param sailorId The sailor ID.
   * @return The contract object.
   */
  Contract getContractBySailorId(String sailorId);

  /**
   * Updates the contract by the contract ID.
   *
   * @param contractId The contract ID.
   * @return The updated contract object.
   */
  Contract updateContract(String contractId);

  /**
   * Adds a new contract to the system.
   *
   * @param contract The contract object to be added.
   * @return The newly added contract with a generated ID.
   */
  Contract addContract(Contract contract);

  Contract saveContract(Contract contract);
}
