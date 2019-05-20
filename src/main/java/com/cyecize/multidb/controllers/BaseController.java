package com.cyecize.multidb.controllers;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

    private static final String REDIRECT_ACTION = "redirect:";

    private ModelAndView finalizeView(ModelAndView modelAndView) {
        //TODO: add additional logic if needed.
        return modelAndView;
    }

    protected ModelAndView redirect(String url) {
        return new ModelAndView(REDIRECT_ACTION + url);
    }

    protected ModelAndView view(String viewName) {
        return this.view(viewName, new ModelAndView());
    }

    protected ModelAndView view(String viewName, String modelName, Object model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(modelName, model);
        return this.view(viewName, modelAndView);
    }

    protected ModelAndView view(String viewName, ModelAndView modelAndView) {
        modelAndView.setViewName(viewName);
        return this.finalizeView(modelAndView);
    }
}
