package com.rsjava.CarAverage.controller;


import com.rsjava.CarAverage.model.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrontEndController {

    @Autowired
    private CarService carService;

    @GetMapping("/")
    public String getCurrency(ModelMap map) {
        map.put("newService", carService);
        return "index";
    }

    @GetMapping("service")
    String getCalc(ModelMap map) {
        map.put("showElements", carService.getNumberOfElements());
        map.put("showAverage", carService.getAverage());
        map.put("showMin", carService.getMinPrice());
        map.put("showMax", carService.getMaxPrice());
        map.put("showMedian", carService.getMedian());
        return "service";
    }

    @PostMapping("/add-service")
    public String addOperation(@ModelAttribute CarService carService) {
        this.carService = carService;
        return "redirect:/service";
    }
}

