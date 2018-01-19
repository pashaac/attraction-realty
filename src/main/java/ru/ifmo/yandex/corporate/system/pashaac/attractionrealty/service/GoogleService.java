package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client.GoogleClient;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;

import java.util.List;

@Service
public class GoogleService {

    private final GoogleClient googleClient;

    public GoogleService(GoogleClient googleClient) {
        this.googleClient = googleClient;
    }

    public boolean isMaxVenues(List<Venue> venues) {
        return venues.size() == googleClient.getVenueSearchLimit();
    }

}
