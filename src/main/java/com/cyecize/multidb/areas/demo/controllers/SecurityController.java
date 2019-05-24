package com.cyecize.multidb.areas.demo.controllers;

import com.cyecize.multidb.areas.demo.bindingModels.CreateUserBindingModel;
import com.cyecize.multidb.areas.demo.services.UserService;
import com.cyecize.multidb.constants.ValidationConstants;
import com.cyecize.multidb.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
@PreAuthorize("isAnonymous()")
public class SecurityController extends BaseController {

    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView loginRequest(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid Username or password");
        }

        return view("security/login");
    }

    @GetMapping("/register")
    public ModelAndView registerRequest(Model model) {
        Map<String, Object> map = model.asMap();

        if (!map.containsKey(ValidationConstants.MODEL_NAME)) {
            model.addAttribute(ValidationConstants.MODEL_NAME, new CreateUserBindingModel());
        }

        return view("security/register");
    }

    @PostMapping("/register")
    public ModelAndView registerAction(@Valid @ModelAttribute("userRegisterModel") CreateUserBindingModel bindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ValidationConstants.BINDING_RESULT_NAME, bindingResult);
        redirectAttributes.addFlashAttribute(ValidationConstants.MODEL_NAME, bindingModel);

        if (bindingResult.hasErrors()) {
            return redirect("register");
        }

        this.userService.createUser(bindingModel);

        return redirect("login");
    }
}
