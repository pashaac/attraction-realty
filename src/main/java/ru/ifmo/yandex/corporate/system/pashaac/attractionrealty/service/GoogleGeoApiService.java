package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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


}
