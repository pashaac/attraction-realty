package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import com.google.maps.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.repository.GeolocationRepository;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util.GeoEarthMathUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class GeolocationService {

    private static final List<AddressComponentType> CITY_COMPONENT_TYPES = Arrays.asList(AddressComponentType.LOCALITY, AddressComponentType.POLITICAL);
    private static final List<AddressType> CITY_TYPES = Arrays.asList(AddressType.LOCALITY, AddressType.POLITICAL);

    private static final List<AddressComponentType> CITY_COMPONENT_TYPES_RESERVE = Arrays.asList(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2, AddressComponentType.POLITICAL);
    private static final List<AddressType> CITY_TYPES_RESERVE = Arrays.asList(AddressType.ADMINISTRATIVE_AREA_LEVEL_2, AddressType.POLITICAL);


    private static final List<AddressComponentType> COUNTRY_COMPONENT_TYPES = Arrays.asList(AddressComponentType.COUNTRY, AddressComponentType.POLITICAL);
    private static final List<AddressType> COUNTRY_TYPES = Arrays.asList(AddressType.COUNTRY, AddressType.POLITICAL);

    private final GoogleGeoService googleGeoService;
    private final GeolocationRepository geolocationRepository;

    @Autowired
    public GeolocationService(GoogleGeoService googleGeoService, GeolocationRepository geolocationRepository) {
        this.googleGeoService = googleGeoService;
        this.geolocationRepository = geolocationRepository;
    }

    public City reverseGeolocation(Marker location) {
        log.info("Try determine city by coordinates ({}, {}) ...", location.getLatitude(), location.getLongitude());
        return geolocationRepository.findAll().stream()
                .filter(city -> GeoEarthMathUtils.contains(city.getBoundingBox(), location))
                .peek(city -> log.info("City {}, {} was found in database", city.getCity(), city.getCountry()))
                .findFirst().orElseGet(() -> reverseGeolocation(googleGeoService.reverseGeocode(location)));
    }

    private City reverseGeolocation(GeocodingResult[] geocodingResults) {
        AddressComponent city = Arrays.stream(geocodingResults)
                .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES))
                .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(CITY_COMPONENT_TYPES))
                .findFirst().orElseGet(() -> Arrays.stream(geocodingResults)
                        .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES_RESERVE))
                        .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                        .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(CITY_COMPONENT_TYPES_RESERVE))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException("Can't determine city geolocation by coordinates")));

        Bounds box = Arrays.stream(geocodingResults)
                .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES))
                .map(geocodingResult -> geocodingResult.geometry.bounds)
                .findFirst().orElseGet(() -> Arrays.stream(geocodingResults)
                        .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(CITY_TYPES_RESERVE))
                        .map(geocodingResult -> geocodingResult.geometry.bounds)
                        .findFirst().orElseThrow(() -> new IllegalArgumentException("Can't determine city boundingbox by coordinates")));

        AddressComponent country = Arrays.stream(geocodingResults)
                .filter(geocodingResult -> Arrays.asList(geocodingResult.types).containsAll(COUNTRY_TYPES))
                .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(COUNTRY_COMPONENT_TYPES))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Can't determine country geolocation by coordinates"));


        BoundingBox boundingBox = new BoundingBox(new Marker(box.southwest.lat, box.southwest.lng), new Marker(box.northeast.lat, box.northeast.lng));
        City savedCity = geolocationRepository.saveAndFlush(new City(city.longName, country.longName, boundingBox));
        log.info("Google geolocation method determined city {}, {} and saved into database", savedCity.getCity(), savedCity.getCountry());
        return savedCity;
    }

    public Marker geolocation(String address) {
        log.info("Try determine city by address {} ...", address);
        return GeoEarthMathUtils.center(reverseGeolocation(googleGeoService.geocode(address)).getBoundingBox());
    }

}
