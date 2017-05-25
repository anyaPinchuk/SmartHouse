package beans.controllers;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api")
public class ConfigurationRestController {

    @GetMapping("/lang")
    public ResponseEntity<?> setLocale(@RequestParam String lang) {
        LocaleContextHolder.setLocale(new Locale(lang));
        return ResponseEntity.ok(null);
    }

}
