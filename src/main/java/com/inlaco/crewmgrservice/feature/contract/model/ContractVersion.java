package com.inlaco.crewmgrservice.feature.contract.model;

import org.springframework.data.mongodb.core.mapping.Document;

/** The document to save all versions of contract */
@Document(collation = "contract_versions")
public class ContractVersion extends Contract {}
