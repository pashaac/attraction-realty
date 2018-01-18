package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller.domain;


import lombok.Builder;
import lombok.Data;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;

import javax.annotation.Nullable;

@Data
@Builder
public class ApiCity {

    private String city;
    private String country;

    @Nullable
    private BoundingBox boundingBox;

}
