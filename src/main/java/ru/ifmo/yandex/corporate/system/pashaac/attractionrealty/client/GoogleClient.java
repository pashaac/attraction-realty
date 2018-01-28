package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util.GeoEarthMathUtils;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util.VenueTitlesUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class GoogleClient {

    private static final Logger logger = LoggerFactory.getLogger(GoogleClient.class);

    @Value("${google.api.call.fail.delay}")
    private Integer delay;

    @Value("${google.venue.search.limit}")
    private Integer limit;

    private final GeoApiContext googleGeoApiContext;

    public GoogleClient(@Value("${google.api.key}") String googleApiKey,
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


    public GeocodingResult[] reverseGeocode(LatLng location) throws InterruptedException, ApiException, IOException {
        return GeocodingApi.reverseGeocode(googleGeoApiContext, location).await();
    }

    public GeocodingResult[] geocode(String address) throws InterruptedException, ApiException, IOException {
        return GeocodingApi.geocode(googleGeoApiContext, address).await();
    }

    public List<Venue> search(BoundingBox boundingBox, String googleCategories) {
        try {
            return apiCall(boundingBox, googleCategories);
        } catch (InterruptedException | ApiException | IOException ignored) {
            logger.info("Sleep for {} milliseconds before request retry...", delay);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.warn("Thread sleep between foursquare API calls was interrupted");
            }
            try {
                return apiCall(boundingBox, googleCategories);
            } catch (InterruptedException | ApiException | IOException e) {
                logger.error("Error during google API call, message {}", e.getMessage());
                return Collections.emptyList();
            }
        }
    }

    private List<Venue> apiCall(BoundingBox boundingBox, String googleCategories) throws InterruptedException, ApiException, IOException {
        Marker center = GeoEarthMathUtils.center(boundingBox);
        int radius = (int) GeoEarthMathUtils.distance(center, boundingBox.getNorthEast());
        PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(googleGeoApiContext, new LatLng(center.getLatitude(), center.getLongitude()))
                .radius(radius)
                .custom("type", googleCategories)
                .rankby(RankBy.DISTANCE)
                .await();

        return Arrays.stream(placesSearchResponse.results)
                .map(venue -> {

                    Venue gVenue = new Venue();

                    gVenue.setTitle(VenueTitlesUtils.titleNormalization(venue.name));
                    gVenue.setDescription(String.format("Contact info:\n\tId: %s\n\tIcon: %s\nTypes: %s\nStatistic info:\n\tRating: %s", venue.placeId,
                            venue.icon, Arrays.toString(venue.types), venue.rating));

                    gVenue.setCategory(categoryValueOf(venue));
                    gVenue.setSource(VenueSource.GOOGLE);

                    gVenue.setLocation(new Marker(venue.geometry.location.lat, venue.geometry.location.lng));
                    gVenue.setAddress(venue.vicinity);

                    gVenue.setRating(venue.rating);


                    if (logger.isDebugEnabled()) {
                        String debugCategoriesStr = Arrays.stream(venue.types)
                                .collect(Collectors.joining("|", "[", "]"));
                        logger.debug("Venue: {}, {}, rating {}, category: {}", gVenue.getTitle(), debugCategoriesStr, gVenue.getRating(), gVenue.getCategory());
                    }
                    return gVenue;
                })
                .collect(Collectors.toList());
    }

    private VenueCategory categoryValueOf(PlacesSearchResult venue) {
        return Arrays.stream(venue.types)
                .map(VenueCategory::valueOfByGoogleKey)
                .filter(Optional::isPresent)
                .findFirst().get().get(); // TODO: fix unchecked .get() calls
    }


    public Integer getVenueSearchLimit() {
        return limit;
    }

}
