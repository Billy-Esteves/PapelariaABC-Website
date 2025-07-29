package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/* 
import com.service.GatewayClientService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import org.springframework.http.*;
import java.util.*;
*/

@Controller
public class WebsiteController {
    
    @PostMapping("/landingPage")
    public String showLandingPage(
            @RequestParam("name") String name,
            @RequestParam("price_min") int price_min,
            @RequestParam("price_max") int price_max,
            @RequestParam("type") String type,
            @RequestParam("ID") int ID,
            Model model) {
        
        // Here you would typically call a service to perform the search
        // For example: gatewayClientService.searchItems(name, price_min, price_max, type, ID);
        
        // Add search results to the model
        model.addAttribute("searchResults", "Results for " + name); // Placeholder for actual results
        
        return "landingPage"; // Return the view name
    }
}
