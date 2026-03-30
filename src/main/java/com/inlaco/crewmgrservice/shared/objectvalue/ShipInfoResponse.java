package com.inlaco.crewmgrservice.shared.objectvalue;

import lombok.Builder;

@Builder
public record ShipInfoResponse(
    String imoNumber,
    String registrationNumber,
    String countryISO,
    String name,
    String description,
    AssetResponse image,
    String type) {}
