package com.cyecize.multidb.areas.demo.controllers;

import com.cyecize.multidb.areas.demo.bindingModels.CreateCarBindingModel;
import com.cyecize.multidb.areas.demo.services.CarService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/cars")
@PreAuthorize("isFullyAuthenticated()")
public class CarsController extends BaseController {

    private final CarService carService;

    @Autowired
    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/browse")
    public ModelAndView listCarsAction() {
        return view("cars/browse-cars", "cars", this.carService.findAll());
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView createCarGetAction(Model model) {
        if (!model.asMap().containsKey(ValidationConstants.MODEL_NAME)) {
            model.addAttribute(ValidationConstants.MODEL_NAME, new CreateCarBindingModel());
        }

        return view("cars/create-car");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView createCarPostAction(@Valid @ModelAttribute CreateCarBindingModel bindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ValidationConstants.MODEL_NAME, bindingModel);
        redirectAttributes.addFlashAttribute(ValidationConstants.BINDING_RESULT_NAME, bindingResult);

        if (bindingResult.hasErrors()) {
            return redirect("create");
        }

        this.carService.createCar(bindingModel);

        return redirect("browse");
    }
}
