package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import com.google.maps.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util.GeoEarthMathUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class GeolocationService {

    private static final Logger logger = LoggerFactory.getLogger(GeolocationService.class);

    private static final List<AddressComponentType> CITY_COMPONENT_TYPES = Arrays.asList(AddressComponentType.LOCALITY, AddressComponentType.POLITICAL);
    private static final List<AddressType> CITY_TYPES = Arrays.asList(AddressType.LOCALITY, AddressType.POLITICAL);

    private static final List<AddressComponentType> CITY_COMPONENT_TYPES_RESERVE = Arrays.asList(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2, AddressComponentType.POLITICAL);
    private static final List<AddressType> CITY_TYPES_RESERVE = Arrays.asList(AddressType.ADMINISTRATIVE_AREA_LEVEL_2, AddressType.POLITICAL);


    private static final List<AddressComponentType> COUNTRY_COMPONENT_TYPES = Arrays.asList(AddressComponentType.COUNTRY, AddressComponentType.POLITICAL);
    private static final List<AddressType> COUNTRY_TYPES = Arrays.asList(AddressType.COUNTRY, AddressType.POLITICAL);

    private final GoogleGeoService googleGeoService;

    @Autowired
    public GeolocationService(GoogleGeoService googleGeoService) {
        this.googleGeoService = googleGeoService;
    }

    public City reverseGeolocation(Marker location) {

        GeocodingResult[] geocodingResults = googleGeoService.reverseGeocode(location);

        AddressComponent city = Arrays.stream(geocodingResults)
                .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES))
                .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(CITY_COMPONENT_TYPES))
                .findFirst().orElseGet(() -> Arrays.stream(geocodingResults)
                        .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES_RESERVE))
                        .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                        .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(CITY_COMPONENT_TYPES_RESERVE))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine city geolocation by coordinates (%s, %s)", location.getLatitude(), location.getLongitude()))));

        Bounds box = Arrays.stream(geocodingResults)
                .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES))
                .map(geocodingResult -> geocodingResult.geometry.bounds)
                .findFirst().orElseGet(() -> Arrays.stream(geocodingResults)
                        .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES_RESERVE))
                        .map(geocodingResult -> geocodingResult.geometry.bounds)
                        .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine city boundingbox by coordinates (%s, %s)", location.getLatitude(), location.getLongitude()))));

        AddressComponent country = Arrays.stream(geocodingResults)
                .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(COUNTRY_TYPES))
                .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(COUNTRY_COMPONENT_TYPES))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine country geolocation by coordinates (%s, %s)", location.getLatitude(), location.getLongitude())));

        logger.info("Google geolocation method determined city: {}, {}", city.longName, country.longName);
        return new City(city.longName, country.longName, new BoundingBox(new Marker(box.southwest.lat, box.southwest.lng), new Marker(box.northeast.lat, box.northeast.lng)));
    }

    public Marker geolocation(String address) {
        Bounds box = Arrays.stream(googleGeoService.geocode(address))
                .map(geocodingResult -> geocodingResult.geometry.bounds)
                .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine coordinates by address %s", address)));
        return GeoEarthMathUtils.center(new BoundingBox(new Marker(box.southwest.lat, box.southwest.lng), new Marker(box.northeast.lat, box.northeast.lng)));
    }

}
