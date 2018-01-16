package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller.domain.ApiCity;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.entity.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.GeolocationService;

/**
 * Created by Pavel Asadchiy
 * on 23:17 15.01.18.
 */
@RestController
@RequestMapping("/geolocation")
@ApiModel(value = "Geolocation logic manager", description = "API to work with project geolocation resources")
public class GeolocationController {

    private final GeolocationService geolocationService;

    public GeolocationController(GeolocationService geolocationService) {
        this.geolocationService = geolocationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Marker golocation(@RequestParam @ApiParam(value = "address of the point", required = true) String  address) {
        return geolocationService.geolocation(address);
    }

    @RequestMapping(path = "/reverse", method = RequestMethod.GET)
    public ApiCity reverseGeolocation(@RequestParam @ApiParam(value = "latitude of the point", required = true) double lat,
                                      @RequestParam @ApiParam(value = "longitude of the point", required = true) double lng) {
        return ApiCity.of(geolocationService.reverseGeolocation(new Marker(lat, lng)));
    }

}
