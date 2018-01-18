package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller.domain;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.BoundingBox;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@Builder
public class ApiCity {

    private String city;
    private String country;

    @Nullable
    private BoundingBox boundingBox;

}
