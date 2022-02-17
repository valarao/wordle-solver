package com.valarao.wordlesolver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to handle mapping point to link frontend client application.
 */
@Controller
public class AppController {

    @RequestMapping({"/"})
    public String loadUI() {
        return "forward:/index.html";
    }
}
