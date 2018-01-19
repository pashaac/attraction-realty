package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.client.FoursquareClient;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.data.BoundingBox;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.City;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.domain.Venue;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.GeolocationService;

import java.util.List;

//@EnableAdminServer doesn't work with swagger :(
@SpringBootApplication
public class AttractionRealtyApplication {

	public static void main(String[] args) {
        SpringApplication.run(AttractionRealtyApplication.class, args);
    }
}
