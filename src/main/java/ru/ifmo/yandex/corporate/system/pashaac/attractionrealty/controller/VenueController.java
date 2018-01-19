package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.FoursquareService;

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

    private final FoursquareService foursquareService;

    public VenueController(FoursquareService foursquareService) {
        this.foursquareService = foursquareService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Venues mining rest point without database communications")
    public List<Venue> mining(@RequestParam @ApiParam(value = "City to searching", required = true) City city,
                              @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "{FOURSQUARE, GOOGLE}") VenueSource source) {
        switch (source) {
            case GOOGLE:
            case FOURSQUARE:
                foursquareService.mine(city);
            default:
                throw new IllegalArgumentException("Incorrect venue source type, possible values: FOURSQUARE, GOOGLE");
        }
    }

}
