package com.inlaco.crewmgrservice.shared.mapstruct.config;

import com.inlaco.crewmgrservice.feature.upload.application.mapper.AssetMapper;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.AssetResponseMapper;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.ObjectIdMapper;
import org.mapstruct.MapperConfig;

@MapperConfig(
    componentModel = "spring",
    uses = {ObjectIdMapper.class, AssetResponseMapper.class, AssetMapper.class})
public interface CentralMapperConfig {}
