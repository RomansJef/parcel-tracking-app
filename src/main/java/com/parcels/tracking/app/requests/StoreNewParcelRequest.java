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
@ApiModel(description = "Request saving new Parcel")
public class StoreNewParcelRequest {
    @ApiModelProperty(notes = "Parcel Id")
    @NotBlank
    private String parcelId;

    @ApiModelProperty(notes = "Client Id")
    @NotBlank
    private String clientId;

    @ApiModelProperty(notes = "Parcel Type")
    @NotBlank
    private String parcelType;

    @ApiModelProperty(notes = "Registration Date")
    private String registrationDate;

    @ApiModelProperty(notes = "Is parcel delivered, by default false")
    private Boolean delivered;

    @ApiModelProperty(notes = "Date of delivery if delivered")
    private String deliveryDate;
}
