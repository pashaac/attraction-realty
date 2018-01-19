package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client.FoursquareClient;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;

import java.util.List;

/**
 *
 * @author Pavel Asadchiy
 */
@Service
public class FoursquareService {

    private final FoursquareClient foursquareClient;

    public FoursquareService(FoursquareClient foursquareClient) {
        this.foursquareClient = foursquareClient;
    }

    public List<Venue> mine(BoundingBox boundingBox, VenueCategory category) {
        return foursquareClient.search(boundingBox, category.getFoursquareKey());
    }

    public boolean isMaxVenues(List<Venue> venues) {
        return venues.size() == foursquareClient.getVenueSearchLimit();
    }

    public List<Venue> venueValidation(List<Venue> venues) {
        return venues; // TODO: implement real validation
    }
}
