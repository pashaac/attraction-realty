package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.data.AbstractVenueMiner;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.data.FoursquareService;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.data.GoogleService;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util.GeoEarthMathUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
public class QuadTreeDataCollector {

    private final FoursquareService foursquareService;
    private final GoogleService googleService;

    public QuadTreeDataCollector(FoursquareService foursquareService, GoogleService googleService) {
        this.foursquareService = foursquareService;
        this.googleService = googleService;
    }

    public List<Venue> mine(City city, VenueSource source, VenueCategory... categories) {
        AbstractVenueMiner venueService = sourceResolver(source);
        long startTime = System.currentTimeMillis();
        List<Venue> venues = new ArrayList<>();
        Queue<BoundingBox> boxQueue = new ArrayDeque<>();
        boxQueue.add(city.getBoundingBox());
        int ind = 0;
        int apiCallCounter = 0;
        while (!boxQueue.isEmpty()) {
            log.debug("Trying to get places {} for boundingbox #{}...", categories, ind++);
            BoundingBox boundingBox = boxQueue.poll();
            List<Venue> boundingBoxVenues = venueService.mine(boundingBox, categories);
            ++apiCallCounter;
            if (venueService.isReachTheLimits(boundingBoxVenues.size())) {
                log.debug("Split bounding box, because {} discovered max amount of venues", source);
                boxQueue.add(GeoEarthMathUtils.leftDownBoundingBox(boundingBox));
                boxQueue.add(GeoEarthMathUtils.leftUpBoundingBox(boundingBox));
                boxQueue.add(GeoEarthMathUtils.rightDownBoundingBox(boundingBox));
                boxQueue.add(GeoEarthMathUtils.rightUpBoundingBox(boundingBox));
                continue;
            }

            int searchedBefore = venues.size();
            venues.addAll(venueService.venueValidation(city, boundingBox, boundingBoxVenues));
            log.info("Was searched: {} %-20 {} venues, filtering: {}", boundingBoxVenues.size(), source, venues.size() - searchedBefore);
        }
        log.info("API called approximately: {} times", apiCallCounter);
        log.info("City area was scanned in {} ms", System.currentTimeMillis() - startTime);
        return venues;
    }

    private AbstractVenueMiner sourceResolver(VenueSource source) {
        switch (source) {
            case GOOGLE:
                return googleService;
            case FOURSQUARE:
                return foursquareService;
            default:
                throw new IllegalArgumentException("Incorrect source type: " + source);
        }
    }

    public List<BoundingBox> reverseMine(City city, List<Venue> venues, VenueSource source) {
        AbstractVenueMiner venueMiner = sourceResolver(source);
        Queue<BoundingBox> boxQueue = new ArrayDeque<>();
        boxQueue.add(city.getBoundingBox());
        List<BoundingBox> boundingBoxes = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        while (!boxQueue.isEmpty()) {
            log.debug("Boundingbox collection size: {}", boundingBoxes.size());
            BoundingBox boundingBox = boxQueue.poll();
            int boundingBoxVenues = 0;
            boolean reachLimit = false;
            for (Venue venue : venues) {
                if (GeoEarthMathUtils.contains(boundingBox, venue.getLocation())) {
                    ++boundingBoxVenues;
                }
                if (venueMiner.isReachTheLimits(boundingBoxVenues)) {
                    reachLimit = true;
                    break;
                }
            }
            if (!reachLimit) {
                boundingBoxes.add(boundingBox);
                continue;
            }
            boxQueue.add(GeoEarthMathUtils.leftDownBoundingBox(boundingBox));
            boxQueue.add(GeoEarthMathUtils.leftUpBoundingBox(boundingBox));
            boxQueue.add(GeoEarthMathUtils.rightDownBoundingBox(boundingBox));
            boxQueue.add(GeoEarthMathUtils.rightUpBoundingBox(boundingBox));
        }
        log.info("BoundingBox map with {} boundingboxes was created in time: {} ms", boundingBoxes.size(), System.currentTimeMillis() - startTime);
        return boundingBoxes;
    }
}
