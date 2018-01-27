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
public class QuadTreeAlgorithmDataCollector {

    private static final Logger logger = LoggerFactory.getLogger(QuadTreeAlgorithmDataCollector.class);

    private final FoursquareService foursquareService;
    private final GoogleService googleService;


    public QuadTreeAlgorithmDataCollector(FoursquareService foursquareService, GoogleService googleService) {
        this.foursquareService = foursquareService;
        this.googleService = googleService;
    }

    public List<Venue> mine(City city, VenueSource source) {
        VenueCategory category = VenueCategory.MUSEUM; // TODO: create normal to search through categories

        long startTime = System.currentTimeMillis();
        List<Venue> venues = new ArrayList<>();
        Queue<BoundingBox> boxQueue = new ArrayDeque<>();
        boxQueue.add(city.getBoundingBox());
        int ind = 0;
        int apiCallCounter = 0;
        while (!boxQueue.isEmpty()) {
            logger.debug("Trying to get places {} for boundingbox #{}...", category, ind++);
            BoundingBox boundingBox = boxQueue.poll();
            List<Venue> boundingBoxVenues = new ArrayList<>();
            switch (source) {
                case GOOGLE:
                case FOURSQUARE:
                    boundingBoxVenues = foursquareService.mine(boundingBox, category);
                    if (foursquareService.isMaxVenues(boundingBoxVenues)) {
                        logger.debug("Split bounding box, because {} discovered max amount of venues", VenueSource.FOURSQUARE);
                        boxQueue.add(GeoEarthMathUtils.leftDownBoundingBox(boundingBox));
                        boxQueue.add(GeoEarthMathUtils.leftUpBoundingBox(boundingBox));
                        boxQueue.add(GeoEarthMathUtils.rightDownBoundingBox(boundingBox));
                        boxQueue.add(GeoEarthMathUtils.rightUpBoundingBox(boundingBox));
                        continue;
                    }
            }
            ++apiCallCounter;

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
