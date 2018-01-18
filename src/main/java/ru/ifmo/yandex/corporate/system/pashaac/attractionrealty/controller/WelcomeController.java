package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Entry point of the service.
 * Redirect base URL on file welcome.html with thymeleaf spring util
 *
 * @author Pavel Asadchiy
 */
@Controller
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

}
