package com.parcels.tracking.app.response;

import com.parcels.tracking.app.ParcelEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ApiModel(description = "Response getting parcels by Client Id")
public class GetParcelsByClientIdResponse {

  @ApiModelProperty(notes = "List of parcels by Client Id")
  private final List<ParcelEntity> parcelsList;
}
