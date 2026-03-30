package com.inlaco.crewmgrservice.shared.mapstruct.mapper;

import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfoResponse;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {AssetResponseMapper.class})
public abstract class ShipInfoResponseMapper {

  public abstract ShipInfoResponse map(ShipInfo shipInfo);
}
