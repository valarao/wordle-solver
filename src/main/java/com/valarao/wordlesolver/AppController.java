package com.valarao.wordlesolver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

    @RequestMapping({"/"})
    public String loadUI() {
        return "forward:/index.html";
    }
}
