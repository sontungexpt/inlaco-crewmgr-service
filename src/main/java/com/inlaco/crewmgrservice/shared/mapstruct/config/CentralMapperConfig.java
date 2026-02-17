package com.inlaco.crewmgrservice.shared.mapstruct.config;

import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.ObjectIdMapper;
import org.mapstruct.MapperConfig;

@MapperConfig(
    componentModel = "spring",
    uses = {ObjectIdMapper.class, AssetResponseMapper.class})
public interface CentralMapperConfig {}
