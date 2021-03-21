package com.parcels.tracking.app;

import com.parcels.tracking.app.requests.*;
import com.parcels.tracking.app.response.*;
import com.parcels.tracking.app.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for Parcels Tracking App.
 */
@Log4j2
@Api(tags = "Parcels Tracking App Controller")
@RestController
public class ParcelsTrackingAppController {

    private final StoreNewParcelService storeNewParcelService;
    private final GetAllParcelsService getAllParcelsService;
    private final GetParcelsByClientIdService getParcelsByClientIdService;
    private final GetByParcelIdService getByParcelIdService;
    private final UpdateParcelService updateParcelService;

    public ParcelsTrackingAppController(StoreNewParcelService storeNewParcelService,
                                        GetAllParcelsService getAllParcelsService,
                                        GetParcelsByClientIdService getParcelsByClientIdService,
                                        GetByParcelIdService getByParcelIdService,
                                        UpdateParcelService updateParcelService) {
        this.storeNewParcelService = storeNewParcelService;
        this.getAllParcelsService = getAllParcelsService;
        this.getParcelsByClientIdService = getParcelsByClientIdService;
        this.getByParcelIdService = getByParcelIdService;
        this.updateParcelService = updateParcelService;
    }

    /**
     * Store new parcel.
     */
    @ApiOperation(value = "Store new parcel", nickname = "saveNewParcel", notes = "Save new parcel in DB")
    @PostMapping(value = "/newParcel", consumes = "application/json")
    public StoreNewParcelResponse storeNewParcel(
            @ApiParam(value = "Store new parcel request data body") @RequestBody @Valid StoreNewParcelRequest request) {
        return storeNewParcelService.executeRequest(request);
    }

    /**
     * Get all parcels from DB.
     */
    @ApiOperation(value = "Show all parcels stored in DB", nickname = "AllParcels", notes = "Show all parcels")
    @GetMapping(value = "/allParcels", produces = "application/json")
    public GetAllParcelsResponse getAllParcels() {
        return getAllParcelsService.executeRequest();
    }

    /**
     * Get parcels by Client Id.
     */
    @ApiOperation(value = "Get parcels by Client Id", nickname = "ParcelsByClientId", notes = "Show all parcels by Client Id")
    @GetMapping(value = "/parcelsByClientId", consumes = "application/json", produces = "application/json")
    public GetParcelsByClientIdResponse getParcelsByClientId(
            @ApiParam(value = "Parcels by Client Id request data body") @RequestBody @Valid GetParcelsByClientIdRequest parcelsByClientId) {
        return getParcelsByClientIdService.executeRequest(parcelsByClientId);
    }

    /**
     * Get by Parcel Id.
     */
    @ApiOperation(value = "Find parcel by Id", nickname = "FindById", notes = "Find parcel by Id")
    @GetMapping(value = "/parcelById", consumes = "application/json", produces = "application/json")
    public GetByParcelIdResponse getParcelById(
            @ApiParam(value = "Parcel by Id request data body") @RequestBody @Valid GetByParcelIdRequest parcelById) {
        return getByParcelIdService.executeRequest(parcelById);
    }

    /**
     * Update by Parcel Id.
     */
    @ApiOperation(value = "Update Parcel", nickname = "UpdateParcel", notes = "Update Parcel")
    @PostMapping(value = "/updateParcel", consumes = "application/json", produces = "application/json")
    public UpdateParcelResponse updateParcel(
            @ApiParam(value = "Update parcel request data body") @RequestBody @Valid UpdateParcelRequest updateParcelRequest) {
        return updateParcelService.executeRequest(updateParcelRequest);
    }
}
