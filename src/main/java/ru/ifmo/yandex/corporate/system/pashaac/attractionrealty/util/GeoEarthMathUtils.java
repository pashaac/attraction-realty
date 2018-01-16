package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util;

import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Marker;

/**
 * Util class for geo calculations on the Earth with geocalc.jap library help
 *
 * @author Pavel Asadchiy
 */
public class GeoEarthMathUtils {

    private static double distance(Marker point1, Marker point2) {
        return EarthCalc.getVincentyDistance(convert(point1), convert(point2));
    }

    private static Point convert(Marker marker) {
        return new Point(new DegreeCoordinate(marker.getLatitude()), new DegreeCoordinate(marker.getLongitude()));
    }

    public static Marker median(Marker point1,Marker point2) {
        double bearing = EarthCalc.getBearing(convert(point1), convert(point2));
        double distance = distance(point1, point2);
        Point median = EarthCalc.pointRadialDistance(convert(point1), bearing, distance / 2);
        return new Marker(median.getLatitude(), median.getLongitude());
    }
}
