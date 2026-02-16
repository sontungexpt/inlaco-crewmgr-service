package com.inlaco.crewmgrservice.shared.mapper;

import org.mapstruct.MapperConfig;

@MapperConfig(
    componentModel = "spring",
    uses = {ObjectIdMapper.class})
public interface CentralMapperConfig {}
