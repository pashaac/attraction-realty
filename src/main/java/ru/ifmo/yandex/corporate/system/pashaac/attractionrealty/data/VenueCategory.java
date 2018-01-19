package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

public enum VenueCategory {
    MUSEUM("", ""),
    PARK("", ""),
    PLAZA("", ""),
    SHRINE("", ""),
    THEATER("", ""),
    FOUNTAIN("", ""),
    GARDEN("", ""),
    PALACE("", ""),
    CASTLE("", "");

    private String foursquareKey;
    private String googleKey;

    VenueCategory(String foursquareKey, String googleKey) {
        this.foursquareKey = foursquareKey;
        this.googleKey = googleKey;
    }

    public static VenueCategory valueOfByFoursquareKey(String foursquareKey) {
        return MUSEUM; // TODO: create normal validator
    }


    public String getFoursquareKey() {
        return foursquareKey;
    }

    public String getGoogleKey() {
        return googleKey;
    }
}
