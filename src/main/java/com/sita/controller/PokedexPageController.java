package com.sita.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sita.dto.PokemonDto;
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
        return "home";
    }

	@GetMapping("/pokedex")
    public String list(Model model) {
		List<PokemonDto> all = pokemonService.getAll();
        model.addAttribute("pokemons", all); 
        return "pokedex";
    }
}
