package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAdminServer TODO: doesn't work with swagger :(

@SpringBootApplication
public class AttractionRealtyApplication {

	public static void main(String[] args) {
        SpringApplication.run(AttractionRealtyApplication.class, args);
    }
}
