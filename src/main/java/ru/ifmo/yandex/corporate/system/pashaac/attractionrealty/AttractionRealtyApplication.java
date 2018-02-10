package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
public class AttractionRealtyApplication {

	public static void main(String[] args) {
        SpringApplication.run(AttractionRealtyApplication.class, args);
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
	    return new MappingJackson2HttpMessageConverter();
    }

}
