package pl.devgroup.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.devgroup.restapi.repository.UserRepository;

@Controller
public class UserController {

    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView getUser() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userRepository.findById(1));
        modelAndView.setViewName("test");
        System.out.println(userRepository.findById(1));

        return modelAndView;
    }
}
