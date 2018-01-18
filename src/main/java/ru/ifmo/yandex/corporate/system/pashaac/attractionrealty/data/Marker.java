package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple point on google map
 *
 * @author Pavel Asadchiy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Marker {

    private double latitude;
    private double longitude;

}
