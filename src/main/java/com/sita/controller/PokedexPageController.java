package com.sita.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sita.service.AuthService;
import com.sita.service.PokemonService;

@Controller
public class PokedexPageController {
	
	private final PokemonService pokemonService;
	private final AuthService authService;
	 
	@Autowired  
	public PokedexPageController(PokemonService pokemonService,
			AuthService authService) {
        this.pokemonService = pokemonService;
        this.authService = authService;
	}
	
	@GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("pokemon", "test"); 
        return "home";
    }

	@GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("pokemon", "test"); 
        return "home";
    }
}
