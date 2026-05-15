package com.mesa.controller;

import com.mesa.model.Reservation;
import com.mesa.model.Restaurant;
import com.mesa.service.ReservationService;
import com.mesa.service.RestaurantService;
import com.mesa.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mesa")
public class MesaController {

    private final RestaurantService restaurantService;
    private final UserService userService;
    private final ReservationService reservationService;

    public MesaController(RestaurantService restaurantService,
                          UserService userService,
                          ReservationService reservationService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping("/homepage")
    public String homepage(Model model) {
        model.addAttribute("topRestaurants",
                restaurantService.getTop4PopularRestaurants());
        model.addAttribute("isLoggedIn", userService.isLoggedIn());
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "mesa";
    }

    @GetMapping("/restaurant/{id}")
    public String restaurantDetails(@PathVariable("id") String id, Model model) {
        model.addAttribute("restaurant",
                restaurantService.getRestaurantById(id));
        model.addAttribute("isLoggedIn", userService.isLoggedIn());
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "restaurant-details";
    }

    @GetMapping("/browse")
    public String browseRestaurants(
            @RequestParam(value = "cuisine", required = false) String cuisine,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "sort", required = false, defaultValue = "reviews") String sort,
            Model model) {

        List<Restaurant> restaurants;

        if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurantService.getRestaurantsByFoodType(cuisine);
            model.addAttribute("pageTitle", capitalize(cuisine) + " Restaurants");
            model.addAttribute("filterDesc", "Exploring " + capitalize(cuisine) + " cuisine");
            model.addAttribute("activeCuisine", cuisine);
        } else if (city != null && !city.isEmpty()) {
            restaurants = restaurantService.getRestaurantsByCity(city);
            model.addAttribute("pageTitle", "Restaurants in " + capitalize(city));
            model.addAttribute("filterDesc", "Dining spots in " + capitalize(city));
            model.addAttribute("activeCity", city);
        } else {
            restaurants = restaurantService.getAllRestaurants();
            model.addAttribute("pageTitle", "All Restaurants");
            model.addAttribute("filterDesc", "Discover amazing dining experiences");
        }

        restaurants = restaurantService.sortRestaurants(restaurants, sort);

        model.addAttribute("restaurants", restaurants);
        model.addAttribute("allRestaurantsCount", restaurants.size());
        model.addAttribute("currentSort", sort);
        model.addAttribute("isLoggedIn", userService.isLoggedIn());
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "browse-restaurants";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        if (userService.login(username, password)) {
            redirectAttributes.addFlashAttribute("success", "Welcome back, " + username + "!");
            return "redirect:/mesa/homepage";
        }
        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/mesa/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         RedirectAttributes redirectAttributes) {
        if (userService.signUp(username, password)) {
            redirectAttributes.addFlashAttribute("success", "Account created! Please log in.");
            return "redirect:/mesa/login";
        }
        redirectAttributes.addFlashAttribute("error", "Username already exists");
        return "redirect:/mesa/signup";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/mesa/homepage";
    }

    @PostMapping("/reserve")
    public String makeReservation(@RequestParam String restaurantId,
                                  @RequestParam String date,
                                  @RequestParam int numberOfPeople,
                                  RedirectAttributes redirectAttributes) {
        if (!userService.isLoggedIn()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to make a reservation");
            return "redirect:/mesa/login";
        }

        Reservation reservation = reservationService.makeReservation(
                userService.getCurrentUser(), restaurantId, date, numberOfPeople
        );

        if (reservation != null) {
            redirectAttributes.addFlashAttribute("success",
                    "Reservation made at " + reservation.getRestaurantName() + " for " + date);
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to make reservation");
        }
        return "redirect:/mesa/restaurant/" + restaurantId;
    }

    @GetMapping("/my-reservations")
    public String myReservations(Model model) {
        if (!userService.isLoggedIn()) {
            return "redirect:/mesa/login";
        }

        List<Reservation> userReservations = reservationService.getUserReservations(
                userService.getCurrentUser()
        );

        model.addAttribute("reservations", userReservations);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentUser", userService.getCurrentUser());

        return "my-reservations";
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Arrays.stream(str.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}