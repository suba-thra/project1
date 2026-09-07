package com.quizapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to route client-side React routes to index.html for HTML5 History API.
 */
@Controller
public class SpaForwardController {

    @RequestMapping(value = {
            "/quizzes/**",
            "/attempts/**",
            "/leaderboard/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
