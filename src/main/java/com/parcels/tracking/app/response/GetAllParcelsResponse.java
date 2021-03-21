package com.parcels.tracking.app.response;

import com.parcels.tracking.app.ParcelEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Response getting all parcels from DB")
public class GetAllParcelsResponse {

    @ApiModelProperty(notes = "List of parcels")
    private List<ParcelEntity> parcelsList;
}
