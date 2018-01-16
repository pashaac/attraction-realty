package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service;

import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import org.springframework.stereotype.Service;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Marker;
import sun.jvm.hotspot.oops.Mark;

@Service
public class GeoEarthMathService {

    public double distance(Marker point1, Marker point2) {
        return EarthCalc.getVincentyDistance(convert(point1), convert(point2));
    }

    private Point convert(Marker marker) {
        return new Point(new DegreeCoordinate(marker.getLatitude()), new DegreeCoordinate(marker.getLongitude()));
    }

    public Marker center(BoundingBox boundingBox) {
        return null; // TODO: need implementation
    }
}
