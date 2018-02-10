package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Bounding box around some area on the Earth
 *
 * @author Pavel Asadchiy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BoundingBox {

    private Marker southWest;
    private Marker northEast;

}
