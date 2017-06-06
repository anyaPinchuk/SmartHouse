package beans.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/lang")
public class LangRESTController {
    private static final Logger LOG = LoggerFactory.getLogger(LangRESTController.class);

    @GetMapping("/get")
    public ResponseEntity<?> getLang(){
        LOG.info("handle get request  by url /api/lang/get");
        return ResponseEntity.ok(LocaleContextHolder.getLocale());
    }

    @GetMapping("/set")
    public ResponseEntity<?> setLang(@RequestParam String lang){
        LOG.info("handle get request with param = {} by url /api/lang/set", lang);
        LocaleContextHolder.setLocale(new Locale(lang));
        return ResponseEntity.ok().build();
    }
}
