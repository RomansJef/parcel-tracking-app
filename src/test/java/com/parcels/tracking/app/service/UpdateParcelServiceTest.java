package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.requests.UpdateParcelRequest;
import com.parcels.tracking.app.response.UpdateParcelResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Log4j2
@RunWith(MockitoJUnitRunner.class)
public class UpdateParcelServiceTest {

    private final ParcelsTrackingAppRepository trackingAppRepository = mock(ParcelsTrackingAppRepository.class);
    private final UpdateParcelService updateParcelService =
            new UpdateParcelService(trackingAppRepository);
    private UpdateParcelRequest updateParcelRequest;
    private ParcelEntity parcelEntity1;
    private ParcelEntity parcelEntity2;
    private UpdateParcelResponse response;

    @Before
    public void setUp() {
        // request
        updateParcelRequest = UpdateParcelRequest.builder()
                .parcelId("parcel0001")
                .delivered(true)
                .deliveryDate("2021-02-18")
                .build();

        // response from repository
        parcelEntity1 = ParcelEntity.builder()
                .id("14c26dee-a039-4019-97cf-51d0aff9b19c")
                .parcelId("parcel0001")
                .clientId("client0001")
                .parcelType("package")
                .registrationDate(LocalDate.of(2021, 2, 17))
                .delivered(false)
                .build();

        // Parcel entity to be stored
        parcelEntity2 = ParcelEntity.builder()
                .id("14c26dee-a039-4019-97cf-51d0aff9b19c")
                .parcelId("parcel0001")
                .clientId("client0001")
                .parcelType("package")
                .registrationDate(LocalDate.of(2021, 2, 17))
                .delivered(true)
                .deliveryDate(LocalDate.of(2021, 2, 18))
                .build();
        // response
        response = UpdateParcelResponse.builder()
                .status("Parcel nr. parcel0001 updated").build();
    }

    @Test
    public void whenUpdateParcelSuccess() {
        when(trackingAppRepository.getByParcelId(updateParcelRequest.getParcelId())).thenReturn(Optional.of(parcelEntity1));
        UpdateParcelResponse actual = updateParcelService.executeRequest(updateParcelRequest);
        verify(trackingAppRepository, times(1)).getByParcelId(updateParcelRequest.getParcelId());
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void whenUpdateParcelNullRepository() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateParcelService.executeRequest(updateParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("367f6b9f-20c1-4304-bdaa-748e09cad243 - Find parcels by Parcel Id error");
    }

    @Test
    public void whenUpdateParcelRequestNull() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateParcelService.executeRequest(null));
        assertThat(exception.getMessage()).isEqualTo("c57ac292-2d43-4e6c-82e2-8f84a6da811a - Missing UpdateParcelRequest");
    }

    @Test
    public void whenUpdateParcelRequestParcelIdFieldNull() {
        // request
        updateParcelRequest.setParcelId(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateParcelService.executeRequest(updateParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("2bdf479b-2dda-4811-acea-38df440284d2 - Missing Parcel ID in UpdateParcelRequest");
    }

    @Test
    public void whenUpdateParcelRequestParcelIdFieldEmpty() {
        // request
        updateParcelRequest.setParcelId("");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateParcelService.executeRequest(updateParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("7a899ea0-da8c-4eb9-9a82-434c238aa6c2 - Parcel Id cannot be empty");
    }

    @Test
    public void whenUpdateParcelRequestParcelWasDeliveredBefore() {
        when(trackingAppRepository.getByParcelId(updateParcelRequest.getParcelId())).thenReturn(Optional.of(parcelEntity2));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateParcelService.executeRequest(updateParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("00263119-5814-41a5-99b4-1a0197bbec9c - Parcel was already delivered");
    }

    @Test
    public void whenUpdateParcelRequestDeliveredTrueAndDeliveryDateNull() {
        // request
        UpdateParcelRequest updateParcelWrongRequest = UpdateParcelRequest.builder()
                .parcelId("parcel0001")
                .delivered(true)
                .build();

        when(trackingAppRepository.getByParcelId(updateParcelRequest.getParcelId())).thenReturn(Optional.of(parcelEntity1));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateParcelService.executeRequest(updateParcelWrongRequest));
        assertThat(exception.getMessage()).isEqualTo("a732d13b-6057-4e4e-8dae-bc069a240fb5 - If parcel is delivered, Delivery Date cannot be null");
    }

    @Test
    public void whenUpdateParcelRequestDeliveredNullAndDeliveryDateNotNull() {
        // request
        UpdateParcelRequest updateParcelWrongRequest = UpdateParcelRequest.builder()
                .parcelId("parcel0001")
                .deliveryDate("2021-02-18")
                .build();

        when(trackingAppRepository.getByParcelId(updateParcelRequest.getParcelId())).thenReturn(Optional.of(parcelEntity1));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateParcelService.executeRequest(updateParcelWrongRequest));
        assertThat(exception.getMessage()).isEqualTo("40c724ce-cf5f-4979-b461-efe22d26dd6f - If parcel is not delivered, Delivery Date cannot be presented");
    }
}
