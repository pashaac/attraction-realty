package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.data;

import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.VenueCategory;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;

import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 15:57 17.02.18.
 */
public abstract class AbstractVenueMiner {
    public abstract List<Venue> mine(BoundingBox boundingBox, VenueCategory... categories);
    public abstract boolean isReachTheLimits(int venues);
    public abstract List<Venue> venueValidation(List<Venue> venues);
}
