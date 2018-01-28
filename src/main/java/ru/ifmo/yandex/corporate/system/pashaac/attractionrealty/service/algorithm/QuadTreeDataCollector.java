package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.FoursquareService;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.GoogleService;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util.GeoEarthMathUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
public class QuadTreeDataCollector {

    private static final Logger logger = LoggerFactory.getLogger(QuadTreeDataCollector.class);

    private final FoursquareService foursquareService;
    private final GoogleService googleService;


    public QuadTreeDataCollector(FoursquareService foursquareService, GoogleService googleService) {
        this.foursquareService = foursquareService;
        this.googleService = googleService;
    }

    public List<Venue> mine(City city, VenueSource source, VenueCategory... categories) {
        long startTime = System.currentTimeMillis();
        List<Venue> venues = new ArrayList<>();
        Queue<BoundingBox> boxQueue = new ArrayDeque<>();
        boxQueue.add(city.getBoundingBox());
        int ind = 0;
        int apiCallCounter = 0;
        while (!boxQueue.isEmpty()) {
            logger.debug("Trying to get places {} for boundingbox #{}...", categories, ind++);
            BoundingBox boundingBox = boxQueue.poll();
            List<Venue> boundingBoxVenues = new ArrayList<>();
            boolean reachTheLimits = false;
            switch (source) {
                case GOOGLE:
                    boundingBoxVenues = googleService.mine(boundingBox, categories);
                    ++apiCallCounter;
                    reachTheLimits = googleService.isReachTheLimits(boundingBoxVenues);
                    break;
                case FOURSQUARE:
                    boundingBoxVenues = foursquareService.mine(boundingBox, categories);
                    ++apiCallCounter;
                    reachTheLimits = foursquareService.isReachTheLimits(boundingBoxVenues);
                    break;
            }
            if (reachTheLimits) {
                logger.debug("Split bounding box, because {} discovered max amount of venues", source);
                boxQueue.add(GeoEarthMathUtils.leftDownBoundingBox(boundingBox));
                boxQueue.add(GeoEarthMathUtils.leftUpBoundingBox(boundingBox));
                boxQueue.add(GeoEarthMathUtils.rightDownBoundingBox(boundingBox));
                boxQueue.add(GeoEarthMathUtils.rightUpBoundingBox(boundingBox));
                continue;
            }


            int searchedBefore = venues.size();
            switch (source) {
                case GOOGLE:
                case FOURSQUARE:
                    venues.addAll(foursquareService.venueValidation(boundingBoxVenues));
            }
            logger.info("Was searched: {} {} venues, filtering: {}", boundingBoxVenues.size(), source, venues.size() - searchedBefore);
        }
        logger.info("API called approximately: {} times", apiCallCounter);
//        logger.info("Total venues was searched and saved: {}", venueService.save(venues).size()); TODO: uncomment after DB implementation
        logger.info("City area was scanned in {} ms", System.currentTimeMillis() - startTime);
        return venues;
    }
}
