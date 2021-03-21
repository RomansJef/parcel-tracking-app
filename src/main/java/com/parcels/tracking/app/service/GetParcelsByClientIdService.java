package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.requests.GetParcelsByClientIdRequest;
import com.parcels.tracking.app.response.GetParcelsByClientIdResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Return parcels by Client Id Service.
 */
@Log4j2
@Service
public class GetParcelsByClientIdService {

    private final ParcelsTrackingAppRepository parcelsTrackingAppRepository;

    public GetParcelsByClientIdService(ParcelsTrackingAppRepository parcelsTrackingAppRepository) {
        this.parcelsTrackingAppRepository = parcelsTrackingAppRepository;
    }

    public GetParcelsByClientIdResponse executeRequest(GetParcelsByClientIdRequest getParcelsByClientIdRequest) {
        Validate.notNull(getParcelsByClientIdRequest, "4d3559ab-03e1-4aa5-ad1a-634a56b8b676 - Missing GetParcelsByClientIdRequest");
        Validate.notNull(getParcelsByClientIdRequest.getClientId(), "a16f38ac-607d-407d-ad79-333842793a1f - Missing Client ID in GetParcelsByClientIdRequest");
        String clientId = getParcelsByClientIdRequest.getClientId();
        Validate.notEmpty(clientId, "02f1ac28-7eba-4643-a3b3-67f234d2e1d6 - Client Id cannot be empty");

        List<ParcelEntity> parcelsByClientId = parcelsTrackingAppRepository.getParcelsByClientId(clientId).orElseThrow(
                () -> new RuntimeException("bb433a3f-8ba0-4684-9379-5163b4f7e207 - Find parcels by Client Id error"));
        return GetParcelsByClientIdResponse.builder().parcelsList(parcelsByClientId).build();
    }
}
