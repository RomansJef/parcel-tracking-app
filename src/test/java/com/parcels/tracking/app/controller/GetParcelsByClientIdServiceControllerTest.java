package com.parcels.tracking.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppController;
import com.parcels.tracking.app.requests.GetParcelsByClientIdRequest;
import com.parcels.tracking.app.response.GetParcelsByClientIdResponse;
import com.parcels.tracking.app.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GetParcelsByClientIdServiceControllerTest {

    private final StoreNewParcelService storeNewParcelService = mock(StoreNewParcelService.class);
    private final GetAllParcelsService getAllParcelsService = mock(GetAllParcelsService.class);
    private final GetParcelsByClientIdService getParcelsByClientIdService =
            mock(GetParcelsByClientIdService.class);
    private final GetByParcelIdService getByParcelIdService = mock(GetByParcelIdService.class);
    private final UpdateParcelService updateParcelService = mock(UpdateParcelService.class);
    private final ParcelsTrackingAppController appController =
            new ParcelsTrackingAppController(
                    storeNewParcelService,
                    getAllParcelsService,
                    getParcelsByClientIdService,
                    getByParcelIdService,
                    updateParcelService);
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new Exception(), appController).build();
    }

    @Test
    public void whenGetParcelsByClientIdSuccess() throws Exception {
        // request entity
        GetParcelsByClientIdRequest getParcelsByClientIdRequest = GetParcelsByClientIdRequest.builder()
                .clientId("client0003").build();

        // response entity
        List<ParcelEntity> parcelsList = new ArrayList<>();
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
        parcelsList.add(parcelEntity);
        GetParcelsByClientIdResponse getParcelsByClientIdResponse = GetParcelsByClientIdResponse.builder()
                .parcelsList(parcelsList).build();
        // when
        when(getParcelsByClientIdService.executeRequest(getParcelsByClientIdRequest)).thenReturn(getParcelsByClientIdResponse);

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        get("/parcelsByClientId")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(getParcelsByClientIdRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.parcelsList").exists())
                .andExpect(jsonPath("$.parcelsList[0].parcelId").exists())
                .andExpect(jsonPath("$.parcelsList[0].clientId").exists())
                .andDo(print());
    }

    @Test
    public void whenGetParcelByClientIdNull() throws Exception {
        // request entity
        GetParcelsByClientIdRequest getParcelsByClientIdRequest = GetParcelsByClientIdRequest.builder()
                .clientId(null).build();

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        get("/parcelsByClientId")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(getParcelsByClientIdRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void whenGetParcelByClientIdRequestNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        get("/parcelsByClientId")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(null)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
