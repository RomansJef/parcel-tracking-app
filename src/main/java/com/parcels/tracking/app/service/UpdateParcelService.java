package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.requests.RequestValidator;
import com.parcels.tracking.app.response.UpdateParcelResponse;
import com.parcels.tracking.app.requests.UpdateParcelRequest;
import com.parcels.tracking.app.ParcelEntity;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Update Parcel Service.
 */
@Log4j2
@Service
public class UpdateParcelService {

    private final ParcelsTrackingAppRepository parcelsTrackingAppRepository;

    public UpdateParcelService(ParcelsTrackingAppRepository parcelsTrackingAppRepository) {
        this.parcelsTrackingAppRepository = parcelsTrackingAppRepository;
    }

    private final UpdateParcelResponse response = new UpdateParcelResponse();
    private final RequestValidator requestValidator = new RequestValidator();

    public UpdateParcelResponse executeRequest(UpdateParcelRequest updateParcelRequest) {
        // Checks request
        Validate.notNull(updateParcelRequest, "c57ac292-2d43-4e6c-82e2-8f84a6da811a - Missing UpdateParcelRequest");
        Validate.notNull(updateParcelRequest.getParcelId(), "2bdf479b-2dda-4811-acea-38df440284d2 - Missing Parcel ID in UpdateParcelRequest");
        String parcelId = updateParcelRequest.getParcelId();
        Validate.notEmpty(parcelId, "7a899ea0-da8c-4eb9-9a82-434c238aa6c2 - Parcel Id cannot be empty");

        ParcelEntity parcelEntity = parcelsTrackingAppRepository.getByParcelId(parcelId).orElseThrow(
                () -> new RuntimeException("367f6b9f-20c1-4304-bdaa-748e09cad243 - Find parcels by Parcel Id error"));

        // Validates delivery conditions
        requestValidator.validateDeliveryConditionsUpdatingParcel(updateParcelRequest, parcelEntity);

        // Updates Delivered field for Parcel Object
        if ((updateParcelRequest.getDelivered() != null) && (!parcelEntity.getDelivered().equals(updateParcelRequest.getDelivered()))) {
            parcelEntity.setDelivered(updateParcelRequest.getDelivered());
        }

        // Checks delivery date
        if (updateParcelRequest.getDeliveryDate() != null && (parcelEntity.getDelivered() || updateParcelRequest.getDelivered())) {
            LocalDate deliveryDate = requestValidator.validateDeliveryDate(updateParcelRequest.getDeliveryDate());
            parcelEntity.setDeliveryDate(deliveryDate);
        }

        parcelsTrackingAppRepository.saveParcel(parcelEntity);
        response.setStatus("Parcel nr. " + parcelId + " updated");
        return response;
    }
}
