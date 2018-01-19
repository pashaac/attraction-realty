package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client.GoogleClient;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;

import java.io.IOException;

/**
 * Service to communicate with google API public services
 *
 * @author Pavel Asadchiy
 */
@Service
public class GoogleGeoService {

    private final GoogleClient googleClient;

    public GoogleGeoService(GoogleClient googleClient) {
        this.googleClient = googleClient;
    }

    public GeocodingResult[] reverseGeocode(Marker location) {
        try {
            return googleClient.reverseGeocode(new LatLng(location.getLatitude(), location.getLongitude()));
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(String.format("Error was happened due to geocoding by coordinates (%s, %s)", location.getLongitude(), location.getLongitude()), e);
        }
    }

    public GeocodingResult[] geocode(String address) {
        try {
            return googleClient.geocode(address);
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(String.format("Error was happened due to geocoding by address: %s", address), e);
        }
    }



}
