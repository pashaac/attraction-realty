package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client.FoursquareClient;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
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

    public List<Venue> mine(City city) {
        String foursquareCategories = "";
        return foursquareClient.search(city.getBoundingBox(), foursquareCategories);
    }
}
