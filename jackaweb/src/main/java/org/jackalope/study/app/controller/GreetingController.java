package org.jackalope.study.app.controller;

import org.jackalope.study.app.entity.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zowie
 *         Email:   hedgehog.zowie@gmail.com
 */
@Controller
public class GreetingController {
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping("/showPerson")
    public @ResponseBody
    Person showPerson() {
        return new Person("hedgehog.zowie","hedgehog.zowie@gmail.com");
    }
}
