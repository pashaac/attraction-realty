package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.repository.VenueRepository;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.algorithm.QuadTreeDataCollector;

import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 20:09 17.02.18.
 */
@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final QuadTreeDataCollector quadTreeDataCollector;

    @Autowired
    public VenueService(VenueRepository venueRepository, QuadTreeDataCollector quadTreeDataCollector) {
        this.venueRepository = venueRepository;
        this.quadTreeDataCollector = quadTreeDataCollector;
    }

    public List<Venue> find(City city, VenueSource source) {
        return venueRepository.findVenuesByCityAndSource(city, source);
    }

    public List<Venue> mine(City city, VenueSource source) {
        List<Venue> venues;
        switch (source) {
            case GOOGLE:
                venues = quadTreeDataCollector.mine(city, VenueSource.GOOGLE, VenueCategory.touristAttractions());
                break;
            case FOURSQUARE:
                venues = quadTreeDataCollector.mine(city, VenueSource.FOURSQUARE, VenueCategory.touristAttractions());
                break;
            default:
                throw new IllegalArgumentException("Incorrect venue source type, possible values: FOURSQUARE, GOOGLE");
        }
        venues = venueRepository.save(venues);
        venueRepository.flush();
        return venues;
    }

    public List<BoundingBox> grid(City city, VenueSource source) {
        List<Venue> venues = find(city, source);
        return quadTreeDataCollector.reverseMine(city, venues, source);
    }
}
