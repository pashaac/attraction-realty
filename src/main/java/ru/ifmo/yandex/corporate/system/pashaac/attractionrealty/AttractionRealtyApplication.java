package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class AttractionRealtyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttractionRealtyApplication.class, args);
	}
}
