package com.hamroyatra.controller;

import com.hamroyatra.model.Destination;
import com.hamroyatra.model.TourPackage;
import com.hamroyatra.service.DestinationService;
import com.hamroyatra.service.TourPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/destinations")
public class DestinationController {
    
    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;
    
    @Autowired
    public DestinationController(DestinationService destinationService, TourPackageService tourPackageService) {
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
    }
    
    @GetMapping
    public String getAllDestinations(Model model) {
        List<Destination> destinations = destinationService.getAllDestinations();
        
        // Add tour packages count for each destination
        for (Destination destination : destinations) {
            List<TourPackage> packages = tourPackageService.getTourPackagesByDestinationId(destination.getId());
            destination.setTourPackages(new java.util.HashSet<>(packages));
        }
        
        model.addAttribute("destinations", destinations);
        model.addAttribute("pageTitle", "Destinations - HamroYatra");
        model.addAttribute("activeTab", "destinations");
        return "destinations/list";
    }
    
    @GetMapping("/{id}")
    public String getDestinationDetails(@PathVariable Long id, Model model) {
        Optional<Destination> destinationOpt = destinationService.getDestinationById(id);
        
        if (destinationOpt.isPresent()) {
            Destination destination = destinationOpt.get();
            List<TourPackage> tourPackages = tourPackageService.getTourPackagesByDestinationId(id);
            
            model.addAttribute("destination", destination);
            model.addAttribute("tourPackages", tourPackages);
            model.addAttribute("pageTitle", destination.getName() + " - HamroYatra");
            model.addAttribute("activeTab", "destinations");
            
            return "destinations/details";
        } else {
            return "redirect:/destinations";
        }
    }
}
