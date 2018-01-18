package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Service to communicate with google API public services
 *
 * @author Pavel Asadchiy
 */
@Service
public class GoogleGeoApiService {

    private final GeoApiContext googleGeoApiContext;

    public GoogleGeoApiService(@Value("${google.api.key}") String googleApiKey,
                               @Value("${google.api.max.queries.per.second}") Integer googleApiMaxQueries,
                               @Value("${google.api.read.timeout}") Integer googleApiReadTimeout,
                               @Value("${google.api.write.timeout}") Integer googleApiWriteTimeout,
                               @Value("${google.api.connect.timeout}") Integer googleApiConnectTimeout) {
        this.googleGeoApiContext = new GeoApiContext.Builder()
                .apiKey(googleApiKey)
                .queryRateLimit(googleApiMaxQueries)
                .readTimeout(googleApiReadTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(googleApiWriteTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(googleApiConnectTimeout, TimeUnit.MILLISECONDS)
                .build();
    }


    public GeocodingResult[] reverseGeocode(Marker location) {
        try {
            return GeocodingApi.reverseGeocode(googleGeoApiContext, new LatLng(location.getLatitude(), location.getLongitude())).await();
        } catch (ApiException | InterruptedException | IOException e) {
            String message = String.format("Error was happened due to geocoding by coordinates (%s, %s)", location.getLongitude(), location.getLongitude());
            throw new RuntimeException(message, e);
        }
    }

    public GeocodingResult[] geocode(String address) {
        try {
            return GeocodingApi.geocode(googleGeoApiContext, address).await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(String.format("Error was happened due to geocoding by address: %s", address), e);
        }
    }

}
