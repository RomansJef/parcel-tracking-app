package com.parcels.tracking.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parcels.tracking.app.ParcelsTrackingAppController;
import com.parcels.tracking.app.requests.GetByParcelIdRequest;
import com.parcels.tracking.app.response.GetByParcelIdResponse;
import com.parcels.tracking.app.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GetByParcelIdServiceControllerTest {

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
    public void whenGetParcelByIdSuccess() throws Exception {
        // request entity
        GetByParcelIdRequest getByParcelIdRequest = GetByParcelIdRequest.builder()
                .parcelId("parcel0003").build();

        // response entity
        GetByParcelIdResponse getAllParcelsResponse =
                GetByParcelIdResponse.builder()
                        .id("id1")
                        .parcelId("parcel0003")
                        .clientId("client0003")
                        .parcelType("flowers")
                        .registrationDate("2021-02-17")
                        .delivered(true)
                        .deliveryDate("2021-02-18")
                        .build();
        // when
        when(getByParcelIdService.executeRequest(getByParcelIdRequest)).thenReturn(getAllParcelsResponse);

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        get("/parcelById")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(getByParcelIdRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.parcelId").exists())
                .andDo(print());
    }

    @Test
    public void whenGetParcelByIdNull() throws Exception {
        // request entity
        GetByParcelIdRequest getByParcelIdRequest = GetByParcelIdRequest.builder()
                .parcelId(null).build();

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        get("/parcelById")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(getByParcelIdRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void whenGetParcelByIdRequestNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        get("/parcelById")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(null)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
