package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Simple point on google map
 *
 * @author Pavel Asadchiy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Marker {

    private double latitude;
    private double longitude;

}
