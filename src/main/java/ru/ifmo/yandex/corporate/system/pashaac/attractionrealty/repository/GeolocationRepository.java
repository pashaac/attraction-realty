package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;

/**
 * Created by Pavel Asadchiy
 * on 20:28 10.02.18.
 */
@Repository
public interface GeolocationRepository extends JpaRepository<City, Long> {
}
