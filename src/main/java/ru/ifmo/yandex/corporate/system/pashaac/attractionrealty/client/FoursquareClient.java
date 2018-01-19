package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class FoursquareClient {

    private static final Logger logger = LoggerFactory.getLogger(FoursquareClient.class);

    @Value("${foursquare.api.call.fail.delay}")
    private Integer delay;

    @Value("${foursquare.venue.search.limit}")
    private Integer limit;

    private final FoursquareApi foursquareApi;

    @Autowired
    public FoursquareClient(@Value("${foursquare.client.id}") String id,
                            @Value("${foursquare.client.secret}") String secret,
                            @Value("${foursquare.redirect.url}") String redirectUrl) {
        this.foursquareApi = new FoursquareApi(id, secret, redirectUrl);
    }

    private Map<String, String> arguments(BoundingBox boundingBox, String foursquareCategories) {
        Map<String, String> params = new HashMap<>();
        params.put("sw", String.format("%s, %s", boundingBox.getSouthWest().getLatitude(), boundingBox.getSouthWest().getLongitude()));
        params.put("ne", String.format("%s, %s", boundingBox.getNorthEast().getLatitude(), boundingBox.getNorthEast().getLongitude()));
        params.put("intent", "browse");
        params.put("limit", String.valueOf(limit));
        params.put("categoryId", foursquareCategories);
        return params;
    }

    private VenueCategory categoryValueOf(CompactVenue venue, String foursquareCategoryIds) {
        return VenueCategory.MUSEUM; // TODO: fix category value of
        //        Arrays.stream(venue.getCategories())
//                .filter(category -> PlaceType.containsOnlyInArg(category.getId(), foursquareCategoryIds))
//                .map(category -> PlaceType.of(category.getId()))
//                .filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    public List<Venue> search(BoundingBox boundingBox, String foursquareCategories) {
        try {
            return apiCall(boundingBox, foursquareCategories);
        } catch (FoursquareApiException ignored) {
            logger.info("Sleep for {} milliseconds before request retry...", delay);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.warn("Thread sleep between foursquare API calls was interrupted");
            }
            try {
                return apiCall(boundingBox, foursquareCategories);
            } catch (FoursquareApiException e) {
                logger.error("Error during foursquare API call, message {}", e.getMessage());
                return Collections.emptyList();
            }
        }
    }

    /**
     * @throws FoursquareApiException when trouble with foursquare API or internet connection
     */
    private List<Venue> apiCall(BoundingBox boundingBox, String foursquareCategories) throws FoursquareApiException {
        Result<VenuesSearchResult> venuesSearchResult = foursquareApi.venuesSearch(arguments(boundingBox, foursquareCategories));

        if (venuesSearchResult.getMeta().getCode() != HttpStatus.OK.value()) {
            logger.error("Foursquare venues search api call return code {}", venuesSearchResult.getMeta().getCode());
            throw new FoursquareApiException("Foursquare venues search api call return code " + venuesSearchResult.getMeta().getCode());
        }

        return Arrays.stream(venuesSearchResult.getResult().getVenues())
                .map(venue -> {
                    Venue fVenue = new Venue();

                    fVenue.setTitle(titleNormalization(venue.getName()));
                    fVenue.setDescription(String.format("Contacts:\t\nPhone: %s\t\nE-mail: %s\t\nTwitter: %s\t\nFacebook: %s\n" +
                                    "URL: %s\n" + "Statistic info:\t\nRating: %s\t\nCheckins: %s\t\nUsers: %s\t\nTip: %s\n",
                            venue.getContact().getFormattedPhone(), venue.getContact().getEmail(), venue.getContact().getTwitter(),
                            venue.getContact().getFacebook(), venue.getUrl(), venue.getRating(), venue.getStats().getCheckinsCount(),
                            venue.getStats().getUsersCount(), venue.getStats().getTipCount()));

                    fVenue.setCategory(categoryValueOf(venue, foursquareCategories));
                    fVenue.setSource(VenueSource.FOURSQUARE);

                    fVenue.setLocation(new Marker(venue.getLocation().getLat(), venue.getLocation().getLng()));
                    fVenue.setAddress(String.format("%s, %s, %s", venue.getLocation().getCountry(), venue.getLocation().getCity(), venue.getLocation().getAddress()));

                    fVenue.setRating(venue.getStats().getCheckinsCount());

                    if (logger.isDebugEnabled()) {
                        String debugCategoriesStr = Arrays.stream(venue.getCategories())
                                .map(category -> category.getName() + " - " + category.getId())
                                .collect(Collectors.joining("|", "[", "]"));
                        logger.debug("Venue: {}, {}, rating {}, category: {}", fVenue.getTitle(), debugCategoriesStr, fVenue.getRating(), fVenue.getCategory());
                    }

                    return fVenue;
                })
                .collect(Collectors.toList());
    }

    private String titleNormalization(String title) {
        String normalizeTitle = title.replaceAll("\\s+", " ").trim();

        Pattern doubleQuotes = Pattern.compile("^(.*)\"(.*)\"(.*)$");
        Pattern singleQuotes = Pattern.compile("^(.*)'(.*)'(.*)$");

        Matcher matcher;
        matcher = doubleQuotes.matcher(normalizeTitle);
        while (matcher.find()) {
            normalizeTitle = matcher.replaceFirst("$1«$2»$3");
        }
        matcher = singleQuotes.matcher(normalizeTitle);
        while (matcher.find()) {
            normalizeTitle = matcher.replaceFirst("$1«$2»$3");
        }

        return normalizeTitle;
    }

    public Integer getVenueSearchLimit() {
        return limit;
    }
}

