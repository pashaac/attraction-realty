package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * City entity with city/country identification variables
 *
 * @author Pavel Asadchiy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    private String city;
    private String country;

}
