package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.Marker;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title", "category"}))
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private VenueCategory category;

    @Enumerated(value = EnumType.STRING)
    private VenueSource source;

    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude")),
    })
    private Marker location;
    private String address;

    private double rating;

    @JsonBackReference("city-venue")
    @ManyToOne(targetEntity = City.class)
    private City city;

}
