package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.data;

import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client.FoursquareClient;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Pavel Asadchiy
 */
@Service
public class FoursquareService extends AbstractVenueMiner {

    private static final String FOURSQUARE_CATEGORIES_SEPARATOR = ",";

    private final FoursquareClient foursquareClient;

    public FoursquareService(FoursquareClient foursquareClient) {
        this.foursquareClient = foursquareClient;
    }

    @Override
    public List<Venue> mine(BoundingBox boundingBox, VenueCategory... categories) {
        return foursquareClient.search(boundingBox, categoriesGrouping(categories));
    }

    @Override
    public boolean isReachTheLimits(int venues) {
        return venues >= foursquareClient.getVenueSearchLimit();
    }

    @Override
    public List<Venue> venueValidation(List<Venue> venues) {
        return venues.stream()
                .filter(venue -> Objects.nonNull(venue.getCategory()))
                .collect(Collectors.toList());
    }


    private String categoriesGrouping(VenueCategory... categories) {
        return Arrays.stream(categories).map(VenueCategory::getFoursquareKey).collect(Collectors.joining(FOURSQUARE_CATEGORIES_SEPARATOR));
    }

}
