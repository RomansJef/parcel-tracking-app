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
@ApiModel(description = "Request getting parcel by ID")
public class GetByParcelIdRequest {

    @ApiModelProperty(notes = "Parcel Id")
    @NotBlank
    private String parcelId;
}
