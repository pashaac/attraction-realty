package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;

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


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Determine point on the Earth by human readable address")
    public Marker searching(@RequestParam @ApiParam(value = "Search area (bounding box)", required = true) BoundingBox boundingBox,
                            @RequestParam @ApiParam(value = "Searching data source", required = true, allowableValues = "{FOURSQUARE, GOOGLE}") VenueSource source) {
        switch (source) {
            case GOOGLE:
            case FOURSQUARE:
        }
    }

}
