package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

import com.google.maps.model.PlaceType;

/**
 * Venue universal categories
 * Foursquare: https://developer.foursquare.com/docs/resources/categories
 * Google: https://developers.google.com/places/supported_types
 */
public enum VenueCategory {
    // Foursquare: Museum
    // Google: Museum
    MUSEUM("4bf58dd8d48988d181941735", PlaceType.MUSEUM),
    // Foursquare: Theme Park, Park, Garden
    // Google: Park
    PARK("4bf58dd8d48988d182941735,4bf58dd8d48988d163941735,4bf58dd8d48988d15a941735", PlaceType.PARK),
    // Foursquare: Theater
    // Google:
    THEATER("4bf58dd8d48988d137941735", null),
    // Foursquare: Spiritual Center
    // Google: Place of worship
    SHRINE("4bf58dd8d48988d131941735", PlaceType.PLACE_OF_WORSHIP),

    PLAZA("", null),
    FOUNTAIN("", null),
    PALACE("", null),

    ART("", null),
    CASTLE("", null);

    private String foursquareKey;
    private PlaceType googleKey;

    VenueCategory(String foursquareKey, PlaceType googleKey) {
        this.foursquareKey = foursquareKey;
        this.googleKey = googleKey;
    }

    public static VenueCategory valueOfByFoursquareKey(String foursquareKey) {
        return MUSEUM; // TODO: create normal validator
    }


    public String getFoursquareKey() {
        return foursquareKey;
    }

    public PlaceType getGoogleKey() {
        return googleKey;
    }
}
