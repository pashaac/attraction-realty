package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.GeolocationService;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.VenueService;

import java.util.List;

/**
 * Controller for working with venues:
 * * search
 * * get
 * * other feature operations
 *
 * @author Pavel Asadchiy
 */
@RestController
@RequestMapping("/venue")
@Api(value = "Venue logic manager", description = "API to work with project venue resources")
public class VenueController {

    private final GeolocationService geolocationService;
    private final VenueService venueService;

    public VenueController(GeolocationService geolocationService, VenueService venueService) {
        this.geolocationService = geolocationService;
        this.venueService = venueService;
    }

    @RequestMapping(value = "/city/attraction", method = RequestMethod.GET)
    @ApiOperation(value = "Get venues from database")
    public List<Venue> getCityAttractions(@RequestParam @ApiParam(value = "City id to find venues", required = true) Long cityId,
                                          @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "FOURSQUARE, GOOGLE") VenueSource source) {
        return venueService.find(geolocationService.find(cityId), source);
    }

    @RequestMapping(value = "/city/attraction/mine", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Venues mining rest point without database communications")
    public List<Venue> mineCityAttractions(@RequestParam @ApiParam(value = "City id to searching", required = true) Long cityId,
                                           @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "FOURSQUARE, GOOGLE") VenueSource source) {

        return venueService.mine(geolocationService.find(cityId), source);
    }

    @RequestMapping(value = "/city/attraction/grid", method = RequestMethod.GET)
    @ApiOperation(value = "Get bounding boxes which emulate Quad-Tree algorithm work")
    public List<BoundingBox> calculateCityAttractionsGrid(@RequestParam @ApiParam(value = "City id to find venues", required = true) Long cityId,
                                                          @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "FOURSQUARE, GOOGLE") VenueSource source) {
        return venueService.grid(geolocationService.find(cityId), source);
    }

}
