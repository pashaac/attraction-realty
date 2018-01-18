package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;

@Data
@NoArgsConstructor
public class Venue {

    private String title;
    private String description;

    private VenueCategory category;
    private VenueSource source;

    private Marker location;
    private String address;

    private double rating;

    private City city;

}
