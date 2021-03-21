package com.parcels.tracking.app.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Request updating parcel")
public class UpdateParcelRequest {

    @ApiModelProperty(notes = "Parcel Id")
    @NotBlank
    private String parcelId;

    @ApiModelProperty(notes = "Is parcel delivered")
    private Boolean delivered;

    @ApiModelProperty(notes = "Parcel delivery date")
    private String deliveryDate;
}
