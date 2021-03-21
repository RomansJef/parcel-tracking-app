package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.requests.GetByParcelIdRequest;
import com.parcels.tracking.app.response.GetByParcelIdResponse;
import lombok.extern.log4j.Log4j2;
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
public class GetByParcelIdServiceTest {

    private final ParcelsTrackingAppRepository trackingAppRepository = mock(ParcelsTrackingAppRepository.class);
    private final GetByParcelIdService getByParcelIdService =
            new GetByParcelIdService(trackingAppRepository);

    @Test
    public void whenGetByParcelIdSuccess() {
        // request
        GetByParcelIdRequest getByParcelIdRequest = GetByParcelIdRequest.builder()
                .parcelId("parcel0001").build();

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
        GetByParcelIdResponse byParcelIdResponse = GetByParcelIdResponse.builder()
                .id("14c26dee-a039-4019-97cf-51d0aff9b19c")
                .parcelId("parcel0001")
                .clientId("client0001")
                .parcelType("package")
                .registrationDate("2021-02-17")
                .delivered(true)
                .deliveryDate("2021-02-18")
                .build();

        when(trackingAppRepository.getByParcelId(getByParcelIdRequest.getParcelId()))
                .thenReturn(Optional.of(parcelEntity));
        GetByParcelIdResponse actual = getByParcelIdService.executeRequest(getByParcelIdRequest);
        verify(trackingAppRepository, times(1)).getByParcelId(getByParcelIdRequest.getParcelId());

        assertThat(actual).isEqualTo(byParcelIdResponse);
    }

    @Test
    public void whenGetByParcelIdNullRepository() {
        // request
        GetByParcelIdRequest getByParcelIdRequest = GetByParcelIdRequest.builder()
                .parcelId("parcel0001").build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getByParcelIdService.executeRequest(getByParcelIdRequest));
        assertThat(exception.getMessage()).isEqualTo("eda35b9a-8f78-4c98-94f1-d0d1f84d7208 - Find by Parcel Id error");
    }

    @Test
    public void whenGetByParcelIdRequestNull() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getByParcelIdService.executeRequest(null));
        assertThat(exception.getMessage()).isEqualTo("62ceb8d7-1c7b-4613-9324-0b4524b7e4e5 - Missing GetByParcelIdRequest");
    }

    @Test
    public void whenGetByParcelIdFieldNull() {
        // request
        GetByParcelIdRequest getByParcelIdRequest = GetByParcelIdRequest.builder()
                .parcelId(null).build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getByParcelIdService.executeRequest(getByParcelIdRequest));
        assertThat(exception.getMessage()).isEqualTo("4f23ea8e-563e-444e-93ab-c37d0bcd8575 - Missing ParcelId in GetByParcelIdRequest");
    }

    @Test
    public void whenGetByParcelIdFieldEmpty() {
        // request
        GetByParcelIdRequest getByParcelIdRequest = GetByParcelIdRequest.builder()
                .parcelId("").build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getByParcelIdService.executeRequest(getByParcelIdRequest));
        assertThat(exception.getMessage()).isEqualTo("20bf21cf-aa21-48d2-8e4c-c58929c5dc7f - ParcelId in GetByParcelIdRequest is empty");
    }
}
