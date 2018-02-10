package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

import java.util.Objects;
import java.util.Optional;

/**
 * Venue universal categories
 * Foursquare: https://developer.foursquare.com/docs/resources/categories
 * Google: https://developers.google.com/places/supported_types
 */
public enum VenueCategory {
    // Foursquare: Museum
    // Google: Museum
    MUSEUM("4bf58dd8d48988d181941735", "museum"),
    // Foursquare: Theme Park, Park, Garden, Forest, National park
    // Google: Park
    PARK("4bf58dd8d48988d182941735,4bf58dd8d48988d163941735,4bf58dd8d48988d15a941735,52e81612bcbc57f1066b7a23,52e81612bcbc57f1066b7a21", "park"),
    // Foursquare: Theater, Opera house
    // Google:
    THEATER("4bf58dd8d48988d137941735,4bf58dd8d48988d136941735", null),
    // Foursquare: Spiritual Center
    // Google: Place of worship
    SHRINE("4bf58dd8d48988d131941735", "place_of_worship"),
    // Foursquare: Street art, Water park, Zoo, Beach, Bridge, Castle, Fountain, Palace, Pedestrian Plaza, Plaza
    // Google: Point of interest
    POINT_OF_INTEREST("507c8c4091d498d9fc8c67a9,4bf58dd8d48988d193941735,4bf58dd8d48988d17b941735,4bf58dd8d48988d1e2941735,4bf58dd8d48988d1df941735" +
            ",50aaa49e4b90af0d42d5de11,56aa371be4b08b9a8d573547,52e81612bcbc57f1066b7a14,52e81612bcbc57f1066b7a25,4bf58dd8d48988d164941735", "point_of_interest");

    // TODO: add bars / party places etc.

    private String foursquareKey;
    private String googleKey;

    VenueCategory(String foursquareKey, String googleKey) {
        this.foursquareKey = foursquareKey;
        this.googleKey = googleKey;
    }

    public String getFoursquareKey() {
        return foursquareKey;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public static Optional<VenueCategory> valueOfByFoursquareKey(String foursquareKey) {
        for (VenueCategory category : values()) {
            if (category.foursquareKey.contains(foursquareKey)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    public static Optional<VenueCategory> valueOfByGoogleKey(String googleKey) {
        for (VenueCategory category : values()) {
            if (Objects.isNull(category.googleKey)) {
                continue;
            }
            if (category.googleKey.contains(googleKey)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    public static VenueCategory[] touristAttractions() {
        return new VenueCategory[]{MUSEUM, PARK}; // TODO: temporary for debug, THEATER, SHRINE, POINT_OF_INTEREST};
    }

}
