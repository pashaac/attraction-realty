package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.entity.City;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCity {

    private String city;
    private String country;

    public static ApiCity of(City city) {
        return new ApiCity(city.getCity(), city.getCountry());
    }
}
