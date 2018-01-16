package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.conf.SwaggerConfiguration;

//@EnableAdminServer doesn't work with swagger :(

@Import(SwaggerConfiguration.class)
@SpringBootApplication(scanBasePackageClasses = SwaggerConfiguration.class)
public class AttractionRealtyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttractionRealtyApplication.class, args);
	}
}
