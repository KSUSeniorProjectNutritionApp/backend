package edu.kennesaw;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedNutritionController {


    @GetMapping("/")
    @ResponseBody
    public String index(Model model){
        return "hello world";
    }
}
