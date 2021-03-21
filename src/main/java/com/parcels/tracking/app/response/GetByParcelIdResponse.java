package com.parcels.tracking.app.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Response getting entity by Parcel Id")
public class GetByParcelIdResponse {

    @ApiModelProperty(notes = "Id")
    String id;

    @ApiModelProperty(notes = "Parcel Id")
    String parcelId;

    @ApiModelProperty(notes = "Client Id")
    String clientId;

    @ApiModelProperty(notes = "Parcel Type")
    String parcelType;

    @ApiModelProperty(notes = "Parcel registration date")
    String registrationDate;

    @ApiModelProperty(notes = "Parcel delivered to client")
    boolean delivered;

    @ApiModelProperty(notes = "Parcel delivery date")
    String deliveryDate;
}
