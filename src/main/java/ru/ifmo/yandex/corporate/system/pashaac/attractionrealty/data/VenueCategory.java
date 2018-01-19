package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

public enum VenueCategory {
    MUSEUM("", ""),
    PARK,
    PLAZA,
    SHRINE,
    THEATER,
    FOUNTAIN,
    GARDEN,
    PALACE,
    CASTLE;

    private String foursquareKey;
    private String googleKey;

    VenueCategory(String foursquareKey, String googleKey) {
        this.foursquareKey = foursquareKey;
        this.googleKey = googleKey;
    }
}
