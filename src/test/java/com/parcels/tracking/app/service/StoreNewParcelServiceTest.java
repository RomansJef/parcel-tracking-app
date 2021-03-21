package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.requests.StoreNewParcelRequest;
import com.parcels.tracking.app.response.StoreNewParcelResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
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
public class StoreNewParcelServiceTest {

    private final ParcelsTrackingAppRepository trackingAppRepository = mock(ParcelsTrackingAppRepository.class);
    private final StoreNewParcelService storeNewParcelService =
            new StoreNewParcelService(trackingAppRepository);
    private StoreNewParcelRequest storeNewParcelRequest;
    private ParcelEntity parcelEntity1;
    private ParcelEntity parcelEntity2;
    private StoreNewParcelResponse response;
    private List<ParcelEntity> parcelsList;

    @Before
    public void setUp() {
        // request
        storeNewParcelRequest = StoreNewParcelRequest.builder()
                .parcelId("parcel0002")
                .clientId("client0002")
                .parcelType("letter")
                .registrationDate("2021-02-17")
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
                .delivered(true)
                .deliveryDate(LocalDate.of(2021, 2, 18))
                .build();
        parcelsList = new ArrayList<>();
        parcelsList.add(parcelEntity1);

        // Parcel entity to be stored
        parcelEntity2 = ParcelEntity.builder()
                .id("fba7b0a1-312d-427b-80a4-a2384c817a98")
                .parcelId("parcel0002")
                .clientId("client0002")
                .parcelType("letter")
                .registrationDate(LocalDate.of(2021, 2, 17))
                .delivered(true)
                .deliveryDate(LocalDate.of(2021, 2, 18))
                .build();
        // response
        response = StoreNewParcelResponse.builder()
                .status("New parcel parcel0002 stored in Parcels Tracking App Repository").build();
    }

    @Test
    public void whenStoreNewParcelSuccess() {
        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        StoreNewParcelResponse actual = storeNewParcelService.executeRequest(storeNewParcelRequest);
        verify(trackingAppRepository, times(1)).getAllParcels();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void whenStoreNewParcelAlreadyStored() {
        // response from repository
        parcelsList.add(parcelEntity2);

        // response
        response = StoreNewParcelResponse.builder()
                .status("Parcel parcel0002 already presented in Parcels Tracking App Repository").build();

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        StoreNewParcelResponse actual = storeNewParcelService.executeRequest(storeNewParcelRequest);
        verify(trackingAppRepository, times(1)).getAllParcels();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void whenStoreNewParcelWithRequestRegistrationDateAndDeliveredNullSuccess() {
        // request
        storeNewParcelRequest.setRegistrationDate(null);
        storeNewParcelRequest.setDelivered(null);
        storeNewParcelRequest.setDeliveryDate(null);

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        StoreNewParcelResponse actual = storeNewParcelService.executeRequest(storeNewParcelRequest);
        verify(trackingAppRepository, times(1)).getAllParcels();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void whenStoreNewParcelNullRepository() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("3745cb5a-7e7e-461f-b0a0-a79345d285bc - Get all parcels error");
    }

    @Test
    public void whenStoreNewParcelRequestNull() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(null));
        assertThat(exception.getMessage()).isEqualTo("9a1aec3e-8872-4975-bf0f-aef0a9e759c8 - Missing New Parcel Request");
    }

    @Test
    public void whenStoreNewParcelFieldParcelIdNull() {
        // request
        storeNewParcelRequest.setParcelId(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("d87c2346-89ab-4fd3-82a2-7f9ca9987b84 - Parcel ID cannot be null");
    }

    @Test
    public void whenStoreNewParcelFieldParcelIdEmpty() {
        // request
        storeNewParcelRequest.setParcelId("");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("8c40138d-9414-46b7-b447-20311bcba395 - Parcel ID cannot be empty");
    }

    @Test
    public void whenStoreNewParcelFieldClientIdNull() {
        // request
        storeNewParcelRequest.setClientId(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("302eea49-83f4-43d0-9f64-e088dab61d5d - Client ID cannot be null");
    }

    @Test
    public void whenStoreNewParcelFieldClientIdEmpty() {
        // request
        storeNewParcelRequest.setClientId("");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("c01e6ba2-e8b3-44bf-9e81-16050bb06674 - Client ID cannot be empty");
    }

    @Test
    public void whenStoreNewParcelRegistrationDateWrongFormat() {
        // request
        storeNewParcelRequest.setRegistrationDate("2021");

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("d3a30226-5389-4009-8b43-4d685dfe9e42 - Registration Date should be in format yyyy-MM-dd");
    }


    @Test
    public void whenStoreNewParcelDeliveredTrueAndDeliveryDateNull() {
        // request
        storeNewParcelRequest.setDeliveryDate(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("6a7cdd63-f145-4a07-85fa-8db028202ac6 - If parcel is delivered, Delivery Date cannot be null");
    }

    @Test
    public void whenStoreNewParcelDeliveryDateWrongFormat() {
        // request
        storeNewParcelRequest.setDeliveryDate("2021");

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("3572d01e-7f46-437a-b825-7ae8be22d42a - Delivery Date should be in format yyyy-MM-dd");
    }

    @Test
    public void whenStoreNewParcelWithRegistrationDateInFuture() {
        // request
        storeNewParcelRequest.setRegistrationDate(LocalDate.now().plusDays(1).toString());

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("f5b128ef-4813-4b13-a7a1-80f8048b7d80 - Registration Date cannot be in future");
    }

    @Test
    public void whenStoreNewParcelWithDeliveryDateInFuture() {
        // request
        storeNewParcelRequest.setDeliveryDate(LocalDate.now().plusDays(1).toString());

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("9c1132b8-2b45-4dc7-b209-1ab36e86f003 - Delivery Date cannot be in future");
    }

    @Test
    public void whenStoreNewParcelWithDeliveryDateBeforeRegistrationDate() {
        // request
        storeNewParcelRequest.setRegistrationDate("2012-02-18");
        storeNewParcelRequest.setDeliveryDate("2012-02-17");

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeNewParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("84a58171-bd2e-48c2-904e-6ad564d223e4 - Parcel registration date cannot be later than delivery date");
    }

    @Test
    public void whenStoreNewParcelDeliveredNullAndDeliveryDateNotNull() {
        // request
        StoreNewParcelRequest storeWrongParcelRequest = StoreNewParcelRequest.builder()
                .parcelId("parcel0002")
                .clientId("client0002")
                .parcelType("letter")
                .registrationDate("2021-02-17")
                .deliveryDate("2021-02-18")
                .build();

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> storeNewParcelService.executeRequest(storeWrongParcelRequest));
        assertThat(exception.getMessage()).isEqualTo("59a1f173-8ab8-4c5f-8946-c4ca3c1822bb - If parcel is not delivered, Delivery Date cannot be presented");
    }
}
