package com.sita.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	
	@GetMapping("/pokemon/{id}")
	public String getPokemon(@PathVariable int id, Model model) {
	    PokemonDto pokemon = pokemonService.getPokemon(id);

	    if (pokemon == null) {
	        return null;
	    }

	    model.addAttribute("pokemon", pokemon); 
	    
	    return "pokemon";
	}
	
}
