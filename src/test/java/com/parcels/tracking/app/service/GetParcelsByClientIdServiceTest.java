package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.requests.GetParcelsByClientIdRequest;
import com.parcels.tracking.app.response.GetParcelsByClientIdResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Log4j2
@RunWith(MockitoJUnitRunner.class)
public class GetParcelsByClientIdServiceTest {

    private final ParcelsTrackingAppRepository trackingAppRepository = mock(ParcelsTrackingAppRepository.class);
    private final GetParcelsByClientIdService getParcelsByClientIdService =
            new GetParcelsByClientIdService(trackingAppRepository);

    @Test
    public void whenGetParcelsByClientIdSuccess() {
        // request
        GetParcelsByClientIdRequest getParcelsRequest = GetParcelsByClientIdRequest.builder()
                .clientId("client0001").build();

        // entity
        ParcelEntity parcelEntity =
                ParcelEntity.builder()
                        .id("14c26dee-a039-4019-97cf-51d0aff9b19c")
                        .parcelId("parcel0001")
                        .clientId("client0001")
                        .parcelType("package")
                        .registrationDate(LocalDate.of(2021, 2, 17))
                        .delivered(true)
                        .deliveryDate(LocalDate.of(2021, 2, 18))
                        .build();
        // response
        List<ParcelEntity> parcelsList = new ArrayList<>();
        parcelsList.add(parcelEntity);
        GetParcelsByClientIdResponse getParcelsByClientIdResponse = GetParcelsByClientIdResponse.builder()
                .parcelsList(parcelsList).build();

        when(trackingAppRepository.getParcelsByClientId(getParcelsRequest.getClientId()))
                .thenReturn(Optional.of(parcelsList));
        GetParcelsByClientIdResponse actual = getParcelsByClientIdService.executeRequest(getParcelsRequest);
        verify(trackingAppRepository, times(1)).getParcelsByClientId(getParcelsRequest.getClientId());
        assertThat(actual).isEqualTo(getParcelsByClientIdResponse);
    }

    @Test
    public void whenGetParcelsByClientIdNullRepository() {
        // request
        GetParcelsByClientIdRequest getParcelsRequest = GetParcelsByClientIdRequest.builder()
                .clientId("client0001").build();

        when(trackingAppRepository.getParcelsByClientId(getParcelsRequest.getClientId()))
                .thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getParcelsByClientIdService.executeRequest(getParcelsRequest));
        assertThat(exception.getMessage()).isEqualTo("bb433a3f-8ba0-4684-9379-5163b4f7e207 - Find parcels by Client Id error");
    }

    @Test
    public void whenGetParcelsByClientIdRequestNull() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getParcelsByClientIdService.executeRequest(null));
        assertThat(exception.getMessage()).isEqualTo("4d3559ab-03e1-4aa5-ad1a-634a56b8b676 - Missing GetParcelsByClientIdRequest");
    }

    @Test
    public void whenGetParcelsByClientIdFieldNull() {
        // request
        GetParcelsByClientIdRequest getParcelsRequest = GetParcelsByClientIdRequest.builder()
                .clientId(null).build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getParcelsByClientIdService.executeRequest(getParcelsRequest));
        assertThat(exception.getMessage()).isEqualTo("a16f38ac-607d-407d-ad79-333842793a1f - Missing Client ID in GetParcelsByClientIdRequest");
    }

    @Test
    public void whenGetByParcelIdFieldEmpty() {
        // request
        GetParcelsByClientIdRequest getParcelsRequest = GetParcelsByClientIdRequest.builder()
                .clientId("").build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getParcelsByClientIdService.executeRequest(getParcelsRequest));
        assertThat(exception.getMessage()).isEqualTo("02f1ac28-7eba-4643-a3b3-67f234d2e1d6 - Client Id cannot be empty");
    }
}
