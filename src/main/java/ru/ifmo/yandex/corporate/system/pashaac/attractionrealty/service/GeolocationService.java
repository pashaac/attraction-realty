package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.Bounds;
import com.google.maps.model.GeocodingResult;
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

    private final GoogleGeoApiService googleGeoApiService;

    @Autowired
    public GeolocationService(GoogleGeoApiService googleGeoApiService) {
        this.googleGeoApiService = googleGeoApiService;
    }

    public City reverseGeolocation(Marker location) {

        GeocodingResult[] geocodingResults = googleGeoApiService.reverseGeocode(location);

        List<AddressComponentType> cityIdentifiers = Arrays.asList(AddressComponentType.LOCALITY, AddressComponentType.POLITICAL);
        AddressComponent city = Arrays.stream(geocodingResults)
                .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(cityIdentifiers))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine city geolocation by coordinates (%s, %s)", location.getLatitude(), location.getLongitude())));

        List<AddressComponentType> countryIdentifiers = Arrays.asList(AddressComponentType.COUNTRY, AddressComponentType.POLITICAL);
        AddressComponent country = Arrays.stream(geocodingResults)
                .flatMap(geocodingResult -> Arrays.stream(geocodingResult.addressComponents))
                .filter(addressComponent -> Arrays.asList(addressComponent.types).containsAll(countryIdentifiers))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine country geolocation by coordinates (%s, %s)", location.getLatitude(), location.getLongitude())));

        Bounds box = Arrays.stream(geocodingResults)
                .map(geocodingResult -> geocodingResult.geometry.bounds)
                .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine city boundingbox by coordinates (%s, %s)", location.getLatitude(), location.getLongitude())));

        logger.info("Google geolocation method determined city: {}, {}", city.longName, country.longName);
        return new City(city.longName, country.longName, new BoundingBox(new Marker(box.southwest.lat, box.southwest.lng), new Marker(box.northeast.lat, box.northeast.lng)));
    }

    public Marker geolocation(String address) {
        Bounds box = Arrays.stream(googleGeoApiService.geocode(address))
                .map(geocodingResult -> geocodingResult.geometry.bounds)
                .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Can't determine coordinates by address %s", address)));
        return GeoEarthMathUtils.median(new Marker(box.southwest.lat, box.southwest.lng), new Marker(box.northeast.lat, box.northeast.lng));
    }

}
