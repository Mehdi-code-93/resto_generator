package com.example.RestaurantWebsite.controller;

import com.example.RestaurantWebsite.model.Avis;
import com.example.RestaurantWebsite.model.Avisresto;
import com.example.RestaurantWebsite.model.Carte;
import com.example.RestaurantWebsite.model.Reservation;
import com.example.RestaurantWebsite.model.Site;
import com.example.RestaurantWebsite.model.Message;
import com.example.RestaurantWebsite.repository.CarteRepository;
import com.example.RestaurantWebsite.repository.ReservationRepository;
import com.example.RestaurantWebsite.repository.SiteRepository;
import com.example.RestaurantWebsite.repository.AvisRepository;
import com.example.RestaurantWebsite.repository.AvisrestoRepository;
import com.example.RestaurantWebsite.repository.MessageRepository;

import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
// import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class RestaurantWebsiteController {
    Boolean key = false;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private AvisRepository avisRepository;

    @Autowired
    private AvisrestoRepository avisrestoRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MessageRepository messageRepository;





    @GetMapping("/site/{restaurant}/edit")
    public String editSiteForm(@PathVariable String restaurant, Model model) {
    Site site = siteRepository.findByRestaurant(restaurant).orElse(null);
    if (site != null) {
        model.addAttribute("site", site);
        return "editSite";
    }
    return "redirect:/"; // Ou vers une page d'erreur si le restaurant n'est pas trouvé
}




@PostMapping("/site/{restaurant}/update")
public String updateSite(
        @PathVariable String restaurant,
        @RequestParam("restaurant") String updatedRestaurant,
        @RequestParam("adresse") String updatedAdresse,
        @RequestParam("emailContact") String updatedEmailContact,
        @RequestParam("telephone") String updatedTelephone,
        @RequestParam("username") String updatedUsername,
        @RequestParam("password") String updatedPassword,
        @RequestParam("police") String updatedPolice,
        @RequestParam("couleur") String updatedCouleur,
        @RequestParam("textCouleur") String updatedTextCouleur,
        @RequestParam("boutonCouleur") String updatedBoutonCouleur,
        @RequestParam("lienCouleur") String updatedLienCouleur,
        @RequestParam(value = "banniere", required = false) MultipartFile updatedBanniere,
        Model model) {

    Site site = siteRepository.findByRestaurant(restaurant).orElse(null);
    if (site != null) {
        site.setRestaurant(updatedRestaurant);
        site.setAdresse(updatedAdresse);
        site.setEmailcontact(updatedEmailContact);
        site.setTelephone(updatedTelephone);
        site.setUsername(updatedUsername);
        site.setPassword(updatedPassword);
        site.setPolice(updatedPolice);
        site.setCouleur(updatedCouleur);
        site.setTextcouleur(updatedTextCouleur);
        site.setBoutoncouleur(updatedBoutonCouleur);
        site.setLiencouleur(updatedLienCouleur);

        if (updatedBanniere != null && !updatedBanniere.isEmpty()) {
            try {
                site.setBanniere(updatedBanniere.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Erreur lors de l'upload de la bannière.");
                return "editSite"; // Assurez-vous que cette vue existe
            }
        }

        siteRepository.save(site);
        return "redirect:/site/" + updatedRestaurant + "/dashboards"; // Redirige vers le site mis à jour
    }

    model.addAttribute("error", "Site non trouvé.");
    return "error"; // Remplacez par une vue d'erreur appropriée
}













    @GetMapping("/creeSite")
    public String creerSiteForm(Model model) {
        return "creeSite";
    }

    @GetMapping("/site/{restaurant}")
    public String viewSite(@PathVariable String restaurant, Model model) {
    // Trouver le site correspondant au restaurant
    Site site = siteRepository.findByRestaurant(restaurant).orElse(null);

    if (site != null) {

        // L'utilisateur est connecté, récupérez les sites
                List<Site> sites = siteRepository.findAll();
                
                // Récupérez les cartes pour ce site
                List<Carte> cartes = carteRepository.findBySite(site);

                model.addAttribute("cartes", cartes);

                // List<Avis> avis = avisRepositoryRepository.findAll();

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
                model.addAttribute("resto", site.getId());

                // Ajoutez les listes de cartes au modèle
                model.addAttribute("entrees", entrees);
                model.addAttribute("plats", plats);
                model.addAttribute("desserts", desserts);
                model.addAttribute("boissons", boissons);




        // Ajouter les attributs du site au modèle
        model.addAttribute("restaurant", site.getRestaurant());
        model.addAttribute("idresto", site.getId());
        model.addAttribute("police", site.getPolice());
        model.addAttribute("banniere", Base64.getEncoder().encodeToString(site.getBanniere()));
        model.addAttribute("couleur", site.getCouleur());
        model.addAttribute("textCouleur", site.getTextcouleur());
        model.addAttribute("boutonCouleur", site.getBoutoncouleur());
        model.addAttribute("lienCouleur", site.getLiencouleur());
        model.addAttribute("adresse", site.getAdresse());
        model.addAttribute("emailContact", site.getEmailcontact());
        model.addAttribute("username", site.getUsername());
        model.addAttribute("telephone", site.getTelephone());
        model.addAttribute("password", site.getPassword());

        List<Avisresto> avisRestos = avisrestoRepository.findByIdresto(site.getId());
        model.addAttribute("avisRestos", avisRestos);
    }

    return "site"; // Assurez-vous que 'site' correspond à votre vue réelle
}

    @GetMapping("/site/{restaurant}/login")
    public String showLoginForm(@PathVariable String restaurant, Model model) {
        Site site = siteRepository.findByRestaurant(restaurant).orElse(null);
        if (site != null) {
            model.addAttribute("restaurant", site.getRestaurant());
            model.addAttribute("police", site.getPolice());
            model.addAttribute("banniere", Base64.getEncoder().encodeToString(site.getBanniere()));
            model.addAttribute("couleur", site.getCouleur());
            model.addAttribute("textCouleur", site.getTextcouleur());
            model.addAttribute("boutonCouleur", site.getBoutoncouleur());
            model.addAttribute("lienCouleur", site.getLiencouleur());
            model.addAttribute("adresse", site.getAdresse());
            model.addAttribute("emailContact", site.getEmailcontact());
            model.addAttribute("username", site.getUsername());
            model.addAttribute("telephone", site.getTelephone());
            model.addAttribute("password", site.getPassword());
        }
        return "login";
    }

    @PostMapping("/site/{restaurant}/login")
    public String processLogin(@PathVariable String restaurant,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpServletResponse response,  // Ajoutez HttpServletResponse ici
                               Model model) {
        Site site = siteRepository.findByRestaurant(restaurant).orElse(null);
        if (site != null && site.getUsername().equals(username) && site.getPassword().equals(password)) {
            // Connexion réussie
            Cookie connectCookie = new Cookie("connect", "true");
            connectCookie.setMaxAge(20 * 60); // Expire après 20 minutes
            connectCookie.setPath("/"); // Chemin pour lequel le cookie est valide
            connectCookie.setHttpOnly(true); // Empêche l'accès via JavaScript pour plus de sécurité
            response.addCookie(connectCookie);
            // return "redirect:/dashboards";
            return "redirect:/site/{restaurant}/dashboards";
        } else {
            // Connexion échouée
            model.addAttribute("error", "Identifiants invalides.");
            return "login";
        }
    }


    @PostMapping("/creerSite")
    public String handleCreeSite(
        @RequestParam("restaurant") String restaurant,
        @RequestParam("police") String police,
        @RequestParam("banniere") MultipartFile banniere,
        @RequestParam("couleur") String couleur,
        @RequestParam("textCouleur") String textCouleur,
        @RequestParam("boutonCouleur") String boutonCouleur,
        @RequestParam("lienCouleur") String lienCouleur,
        @RequestParam("adresse") String adresse,
        @RequestParam("emailContact") String emailContact,
        @RequestParam("telephone") String telephone,
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        Model model) {
        // Vérifier si un fichier a été téléchargé
        if (!banniere.isEmpty()) {
            try {
                byte[] banniereBytes = banniere.getBytes();

                Site site = new Site();
                site.setRestaurant(restaurant);
                site.setPolice(police);
                site.setBanniere(banniereBytes);
                site.setCouleur(couleur);
                site.setTextcouleur(textCouleur);
                site.setBoutoncouleur(boutonCouleur);
                site.setLiencouleur(lienCouleur);
                site.setAdresse(adresse);
                site.setEmailcontact(emailContact);
                site.setTelephone(telephone);
                site.setUsername(username);
                site.setPassword(password);

                siteRepository.save(site);

                model.addAttribute("restaurant", restaurant);
                model.addAttribute("police", police);
                model.addAttribute("couleur", couleur);
                model.addAttribute("textCouleur", textCouleur);
                model.addAttribute("boutonCouleur", boutonCouleur);
                model.addAttribute("lienCouleur", lienCouleur);
                model.addAttribute("adresse", adresse);
                model.addAttribute("emailContact", emailContact);
                model.addAttribute("telephone", telephone);
                model.addAttribute("username", username);

                return "siteConfirmation";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Erreur lors de l'upload de la bannière.");
                return "creeSite";
            }
        } else {
            model.addAttribute("error", "Veuillez sélectionner une bannière.");
            return "creeSite";
        }
    }




    @PostMapping("/addReview")
    public String addReview(
        @RequestParam("nom") String nom,
        @RequestParam("email") String email,
        @RequestParam("commentaire") String commentaire,
        @RequestParam("plat") String plat,
        @RequestParam("etoile") int etoile,
        @RequestParam("idresto") int idresto,
        @RequestParam("restaurant") String restaurant,
        Model model) {


                Avis avis = new Avis();
                avis.setNom(nom);
                avis.setEmail(email);
                avis.setCommentaire(commentaire);
                avis.setPlat(plat);
                avis.setEtoile(etoile);
                avis.setIdresto(idresto);

            

                avisRepository.save(avis);

                model.addAttribute("nom", nom);
                model.addAttribute("email", email);
                model.addAttribute("commentaire", commentaire);
                model.addAttribute("plat", plat);
                model.addAttribute("etoile", etoile);
                model.addAttribute("idresto", idresto);

                return "redirect:/site/" + restaurant;
            }



            @PostMapping("/avisResto")
            public String addAvisresto(
                @RequestParam("nom") String nom,
                @RequestParam("email") String email,
                @RequestParam("commentaire") String commentaire,
                @RequestParam("etoile") int etoile,
                @RequestParam("idresto") int idresto,
                @RequestParam("restaurant") String restaurant,
                Model model) {
        
        
                        Avisresto avisresto = new Avisresto();
                        avisresto.setNom(nom);
                        avisresto.setEmail(email);
                        avisresto.setCommentaire(commentaire);
                        avisresto.setEtoile(etoile);
                        avisresto.setIdresto(idresto);
        
                    
        
                        avisrestoRepository.save(avisresto);
        
                        model.addAttribute("nom", nom);
                        model.addAttribute("email", email);
                        model.addAttribute("commentaire", commentaire);
                        model.addAttribute("etoile", etoile);
                        model.addAttribute("idresto", idresto);
        
                        return "redirect:/site/" + restaurant;
                    }

                    @PostMapping("/liked")
                    public String likeAvis(@RequestParam("id") Long id, @RequestParam("anchor") String anchor, @RequestParam("restaurant") String restaurant, RedirectAttributes redirectAttributes) {
                        Optional<Avisresto> optionalAvis = avisrestoRepository.findById(id);
                        if (optionalAvis.isPresent()) {
                            Avisresto avis = optionalAvis.get();
                            avis.setLiked(avis.getLiked() + 1);
                            avisrestoRepository.save(avis);
                        }
                        redirectAttributes.addFlashAttribute("scrollTo", anchor);
                        return "redirect:/site/" + restaurant + "#" + anchor;
                    }
                    
                    @PostMapping("/dislike")
                    public String dislikeAvis(@RequestParam("id") Long id, @RequestParam("anchor") String anchor, @RequestParam("restaurant") String restaurant, RedirectAttributes redirectAttributes) {
                        Optional<Avisresto> optionalAvis = avisrestoRepository.findById(id);
                        if (optionalAvis.isPresent()) {
                            Avisresto avis = optionalAvis.get();
                            avis.setDislike(avis.getDislike() + 1);
                            avisrestoRepository.save(avis);
                        }
                        redirectAttributes.addFlashAttribute("scrollTo", anchor);
                        return "redirect:/site/" + restaurant + "#" + anchor;
                    } 






            @PostMapping("/sites")
            public String addReservation(
                @RequestParam("nom") String nom,
                @RequestParam("email") String email,
                @RequestParam("commentaire") String commentaire,
                @RequestParam("phone") int phone,
                @RequestParam("personne") int personne,
                @RequestParam("idresto") int idresto,
                @RequestParam("restaurant") String restaurant,
                Model model) {
        
        
                        Reservation reservation = new Reservation();
                        reservation.setNom(nom);
                        reservation.setEmail(email);
                        reservation.setCommentaire(commentaire);
                        reservation.setPersonne(personne);
                        reservation.setPhone(phone);
                        reservation.setIdresto(idresto);
        
                    
        
                        reservationRepository.save(reservation);
        
                        model.addAttribute("nom", nom);
                        model.addAttribute("email", email);
                        model.addAttribute("commentaire", commentaire);
                        model.addAttribute("phone", phone);
                        model.addAttribute("personne", personne);
                        model.addAttribute("idresto", idresto);
        
                        return "redirect:/site/" + restaurant;
                    }





            @PostMapping("/message")
            public String addMessage(
                @RequestParam("nom") String nom,
                @RequestParam("email") String email,
                @RequestParam("commentaire") String commentaire,
                @RequestParam("phone") int phone,
                @RequestParam("idresto") int idresto,
                @RequestParam("restaurant") String restaurant,
                Model model) {
        
        
                    Message message = new Message();
                    message.setNom(nom);
                    message.setEmail(email);
                    message.setCommentaire(commentaire);
                    message.setPhone(phone);
                    message.setIdresto(idresto);
        
                    
        
                    messageRepository.save(message);
        
                        model.addAttribute("nom", nom);
                        model.addAttribute("email", email);
                        model.addAttribute("commentaire", commentaire);
                        model.addAttribute("phone", phone);
                        model.addAttribute("idresto", idresto);
        
                        return "redirect:/site/" + restaurant;
                    }
            
    

}
