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

//@Import(SwaggerConfiguration.class)
@SpringBootApplication

public class AttractionRealtyApplication {

	public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(AttractionRealtyApplication.class, args);

        FoursquareClient foursquareClient = context.getBean(FoursquareClient.class);
        GeolocationService geolocationService = context.getBean(GeolocationService.class);

        BoundingBox boundingBox = geolocationService.boundingBoxGeolocation(new City("Saint-Petersburg", "Russia"));
        List<Venue> venues = foursquareClient.search(boundingBox, "4bf58dd8d48988d137941735");

        System.out.println(venues);

    }
}
