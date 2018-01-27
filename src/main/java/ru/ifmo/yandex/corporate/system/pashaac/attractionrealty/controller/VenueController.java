package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.GeolocationService;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.algorithm.QuadTreeAlgorithmDataCollector;

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

    private final QuadTreeAlgorithmDataCollector quadTreeAlgorithmDataCollector;
    private final GeolocationService geolocationService;

    public VenueController(QuadTreeAlgorithmDataCollector quadTreeAlgorithmDataCollector, GeolocationService geolocationService) {
        this.quadTreeAlgorithmDataCollector = quadTreeAlgorithmDataCollector;
        this.geolocationService = geolocationService;
    }

    @RequestMapping(value = "/mine/city" ,method = RequestMethod.GET)
    @ApiOperation(value = "Venues mining rest point without database communications")
    public List<Venue> mining(@RequestBody @ApiParam(value = "City to searching", required = true) City city,
                              @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "FOURSQUARE, GOOGLE") VenueSource source) {
        switch (source) {
            case GOOGLE:
            case FOURSQUARE:
                return quadTreeAlgorithmDataCollector.mine(city, VenueSource.FOURSQUARE);
            default:
                throw new IllegalArgumentException("Incorrect venue source type, possible values: FOURSQUARE, GOOGLE");
        }
    }

    @RequestMapping(value = "/mine/coordinates", method = RequestMethod.GET)
    @ApiOperation(value = "Venues mining rest point without database communications")
    public List<Venue> mining(@RequestParam @ApiParam(value = "some city latitude", required = true) double lat,
                              @RequestParam @ApiParam(value = "some city longitude", required = true) double lng,
                              @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "FOURSQUARE, GOOGLE") VenueSource source) {
        return mining(geolocationService.reverseGeolocation(new Marker(lat, lng)), source);
    }

}
