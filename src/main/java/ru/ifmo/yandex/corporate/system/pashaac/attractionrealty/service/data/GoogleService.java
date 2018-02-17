package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.data;

import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client.GoogleClient;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoogleService extends AbstractVenueMiner {

    private static final String GOOGLE_PLACE_TYPES_SEPARATOR = "|";

    private final GoogleClient googleClient;

    public GoogleService(GoogleClient googleClient) {
        this.googleClient = googleClient;
    }

    @Override
    public boolean isReachTheLimits(int venues) {
        return venues >= googleClient.getVenueSearchLimit();
    }

    @Override
    public List<Venue> mine(BoundingBox boundingBox, VenueCategory... categories) {
        return googleClient.search(boundingBox, categoriesGrouping(categories));
    }

    @Override
    public List<Venue> venueValidation(List<Venue> venues) {
        return venues.stream()
                .filter(venue -> Objects.nonNull(venue.getCategory()))
                .collect(Collectors.toList());
    }

    private String categoriesGrouping(VenueCategory... categories) {
        return Arrays.stream(categories).map(VenueCategory::getGoogleKey).collect(Collectors.joining(GOOGLE_PLACE_TYPES_SEPARATOR));
    }

}
