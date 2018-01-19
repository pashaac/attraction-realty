package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

import com.google.maps.model.PlaceType;

public enum VenueCategory {
    MUSEUM("", null),
    PARK("", null),
    PLAZA("", null),
    SHRINE("", null),
    THEATER("", null),
    FOUNTAIN("", null),
    GARDEN("", null),
    PALACE("", null),
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
