package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Created by Pavel Asadchiy
 * on 23:21 15.01.18.
 */
@Embeddable
@Data
@RequiredArgsConstructor
public class Marker {

    private final double latitude;
    private final double longitude;

}
