package app.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping
    String handleMain(){
        return "Welcome to OverBrain!!!";
    }
}
