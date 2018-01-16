package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller.GeolocationController;
import ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.service.GeolocationService;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Pavel Asadchiy
 * on 23:18 15.01.18.
 */
@EnableSwagger2
@ComponentScan(basePackageClasses = {GeolocationController.class, GeolocationService.class})
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName("Attraction realty")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Attraction realty")
                .description("Geo worldwide investment platform for real estate objects based / influenced on tourist attractions")
                .contact(new Contact("Pavel Asadchiy", "vk.com/pasha_ac", "pavel.asadchiy@gmail.com"))
                .version("1.0")
                .build();
    }

}
