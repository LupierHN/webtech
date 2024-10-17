package de.htwberlin.webtech.webtech;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestExampleController {
    @GetMapping(path = "/hero")
    public ResponseEntity<Hero> getHero(){
        final Hero hero = new Hero("Han Solo","Rebellion",1.85);
        return ResponseEntity.ok(hero);
    }
}

