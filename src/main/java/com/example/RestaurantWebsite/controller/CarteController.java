package com.example.RestaurantWebsite.controller;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import com.example.RestaurantWebsite.model.Carte;
import com.example.RestaurantWebsite.model.Site;
import com.example.RestaurantWebsite.repository.CarteRepository;
import com.example.RestaurantWebsite.repository.SiteRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CarteController {

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private SiteRepository siteRepository;









    @GetMapping("/site/{restaurant}/dashboards/edit/{id}")
    public String editCarte(@PathVariable String restaurant, @PathVariable Long id, Model model) {
        Carte carte = carteRepository.findById(id).orElse(null);
        if (carte == null) {
            model.addAttribute("error", "Plat non trouvé.");
            return "dashboards";
        }
        model.addAttribute("carte", carte);
        model.addAttribute("restaurant", restaurant);
        return "editCarte"; // Assurez-vous que 'editCarte' correspond à votre vue réelle pour l'édition
    }
    
    @PostMapping("/site/{restaurant}/dashboards/edit/{id}")
    public String updateCarte(
        @PathVariable String restaurant,
        @PathVariable Long id,
        @RequestParam("nom") String nom,
        @RequestParam("description") String description,
        @RequestParam("prix") int prix,
        @RequestParam("image") MultipartFile image,
        @RequestParam("categorie") String categorie,
        Model model) {
    
        Carte carte = carteRepository.findById(id).orElse(null);
        if (carte == null) {
            model.addAttribute("error", "Plat non trouvé.");
            return "dashboards";
        }
    
        carte.setNom(nom);
        carte.setDescription(description);
        carte.setPrix(prix);
        carte.setCategorie(categorie);
    
        if (!image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();
                carte.setImage(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Erreur lors de l'upload de l'image.");
                return "dashboards";
            }
        }
    
        carteRepository.save(carte);
        return "redirect:/site/" + restaurant + "/dashboards";
    }
    
    @GetMapping("/site/{restaurant}/dashboards/delete/{id}")
    public String deleteCarte(@PathVariable String restaurant, @PathVariable Long id, Model model) {
        carteRepository.deleteById(id);
        return "redirect:/site/" + restaurant + "/dashboards";
}









@GetMapping("/site/{restaurant}/dashboards")
public String dashboards(@PathVariable String restaurant, HttpServletRequest request, Model model) {
    Site site = siteRepository.findByRestaurant(restaurant).orElse(null);
    jakarta.servlet.http.Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (jakarta.servlet.http.Cookie cookie : cookies) {
            if ("connect".equals(cookie.getName()) && "true".equals(cookie.getValue())) {
                // L'utilisateur est connecté, récupérez les sites
                List<Site> sites = siteRepository.findAll();
                
                // Récupérez les cartes pour ce site
                List<Carte> cartes = carteRepository.findBySite(site);

                // Filtrer les cartes par catégorie
                List<Carte> entrees = cartes.stream()
                                            .filter(carte -> "entree".equals(carte.getCategorie()))
                                            .collect(Collectors.toList());
                List<Carte> plats = cartes.stream()
                                          .filter(carte -> "plat".equals(carte.getCategorie()))
                                          .collect(Collectors.toList());
                List<Carte> desserts = cartes.stream()
                                             .filter(carte -> "dessert".equals(carte.getCategorie()))
                                             .collect(Collectors.toList());
                List<Carte> boissons = cartes.stream()
                                             .filter(carte -> "boisson".equals(carte.getCategorie()))
                                             .collect(Collectors.toList());

                model.addAttribute("sites", sites);
                model.addAttribute("restaurant", site.getRestaurant());
                model.addAttribute("resto", site.getId());

                // Ajoutez les listes de cartes au modèle
                model.addAttribute("entrees", entrees);
                model.addAttribute("plats", plats);
                model.addAttribute("desserts", desserts);
                model.addAttribute("boissons", boissons);

                return "/dashboards"; // Assurez-vous que 'dashboards' correspond à votre vue réelle
            }
        }
    }
    // Redirigez vers la page de connexion si l'utilisateur n'est pas authentifié
    return "/login";
}





    @PostMapping("/site/{restaurant}/dashboards")
    public String handleCreeCarte(
        @RequestParam("nom") String nom,
        @RequestParam("description") String description,
        @RequestParam("prix") int prix,
        @RequestParam("image") MultipartFile image,
        @RequestParam("categorie") String categorie,
        @RequestParam("resto") Long resto,
        @PathVariable String restaurant,
        Model model) {

        if (!image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();

                Carte carte = new Carte();
                carte.setNom(nom);
                carte.setDescription(description);
                carte.setPrix(prix);
                carte.setImage(imageBytes);
                carte.setCategorie(categorie);

                // Associer la carte au site
                Site site = siteRepository.findById(resto).orElse(null);
                if (site == null) {
                    model.addAttribute("error", "Restaurant non trouvé.");
                    return "dashboards";
                }
                carte.setSite(site);

                carteRepository.save(carte);

                model.addAttribute("nom", nom);
                model.addAttribute("description", description);
                model.addAttribute("prix", prix);
                model.addAttribute("categorie", categorie);

                return "redirect:/site/" + restaurant + "/dashboards";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Erreur lors de l'upload de l'image.");
                return "dashboards";
            }
        } else {
            model.addAttribute("error", "Veuillez sélectionner une image.");
            return "dashboards";
        }
    }
}
