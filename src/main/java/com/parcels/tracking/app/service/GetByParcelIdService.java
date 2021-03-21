package com.parcels.tracking.app.service;

import com.parcels.tracking.app.requests.GetByParcelIdRequest;
import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.response.GetByParcelIdResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

/**
 * Find by Parcel Id Service.
 */
@Log4j2
@Service
public class GetByParcelIdService {

    private final ParcelsTrackingAppRepository parcelsTrackingAppRepository;

    public GetByParcelIdService(ParcelsTrackingAppRepository parcelsTrackingAppRepository) {
        this.parcelsTrackingAppRepository = parcelsTrackingAppRepository;
    }

    public GetByParcelIdResponse executeRequest(GetByParcelIdRequest getByParcelIdRequest) {
        // Checks request
        Validate.notNull(getByParcelIdRequest, "62ceb8d7-1c7b-4613-9324-0b4524b7e4e5 - Missing GetByParcelIdRequest");
        Validate.notNull(getByParcelIdRequest.getParcelId(), "4f23ea8e-563e-444e-93ab-c37d0bcd8575 - Missing ParcelId in GetByParcelIdRequest");
        String parcelId = getByParcelIdRequest.getParcelId();
        Validate.notEmpty(parcelId, "20bf21cf-aa21-48d2-8e4c-c58929c5dc7f - ParcelId in GetByParcelIdRequest is empty");

        ParcelEntity parcelEntityById = parcelsTrackingAppRepository.getByParcelId(parcelId).orElseThrow(
                () -> new RuntimeException("eda35b9a-8f78-4c98-94f1-d0d1f84d7208 - Find by Parcel Id error"));

        GetByParcelIdResponse response = GetByParcelIdResponse.builder()
                .id(parcelEntityById.getId())
                .parcelId(parcelEntityById.getParcelId())
                .clientId(parcelEntityById.getClientId())
                .parcelType(parcelEntityById.getParcelType())
                .registrationDate(parcelEntityById.getRegistrationDate().toString())
                .delivered(parcelEntityById.getDelivered())
                .build();

        // Checks delivery date for null
        if (parcelEntityById.getDeliveryDate() != null) {
            response.setDeliveryDate(parcelEntityById.getDeliveryDate().toString());
        }
        return response;
    }
}
