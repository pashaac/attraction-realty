package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueSource;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;

import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 15:54 17.02.18.
 */
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findVenuesByCityAndSource(City city, VenueSource source);
}
