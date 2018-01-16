package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bounding box around some area on the Earth
 *
 * @author Pavel Asadchiy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoundingBox {

    private Marker southWest;
    private Marker northEast;

}
