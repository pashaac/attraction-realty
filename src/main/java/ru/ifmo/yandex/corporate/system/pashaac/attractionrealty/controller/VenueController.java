package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.algorithm.QuadTreeDataCollector;

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

    private final QuadTreeDataCollector quadTreeDataCollector;

    public VenueController(QuadTreeDataCollector quadTreeDataCollector) {
        this.quadTreeDataCollector = quadTreeDataCollector;
    }

    @RequestMapping(value = "/mine/city/attraction", method = RequestMethod.GET)
    @ApiOperation(value = "Venues mining rest point without database communications")
    public List<Venue> mining(@RequestBody @ApiParam(value = "City to searching", required = true) City city,
                              @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "FOURSQUARE, GOOGLE") VenueSource source) {
        switch (source) {
            case GOOGLE:
                return quadTreeDataCollector.mine(city, VenueSource.GOOGLE, VenueCategory.touristAttractions());
            case FOURSQUARE:
                return quadTreeDataCollector.mine(city, VenueSource.FOURSQUARE, VenueCategory.touristAttractions());
            default:
                throw new IllegalArgumentException("Incorrect venue source type, possible values: FOURSQUARE, GOOGLE");
        }
    }

}
