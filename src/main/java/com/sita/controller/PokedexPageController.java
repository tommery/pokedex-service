package com.sita.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sita.dto.PagedResult;
import com.sita.dto.PokemonDto;
import com.sita.service.AuthService;
import com.sita.service.PokemonService;

import jakarta.servlet.http.HttpServletRequest;

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

	@GetMapping("/collection")
	public String getUserCollection(HttpServletRequest request) {
	    return "collection";
	}
	
	@GetMapping("/pokedex")
    public String list(Model model) {
		List<PokemonDto> all = pokemonService.getAll();
        model.addAttribute("pokemons", all); 
        return "pokedex";
    }
	
	@GetMapping("/list")
	public String list(Model model,
			@RequestParam(required = false) String name,
	        @RequestParam(required = false) String type,
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int size) {

		size = Math.min(size, 100);  // max size = 100
	    size = Math.max(size, 10);   // min size = 10

	    int internalPage = Math.max(0, page - 1);
	    
	    PagedResult<PokemonDto> result =
	            pokemonService.getFiltered(name, type, internalPage, size);
	    
	    model.addAttribute("pokemons", result.getItems()); 
	    model.addAttribute("total", result.getTotal()); 
	    model.addAttribute("page", result.getPage()); 
	    model.addAttribute("totalPages", result.getTotal()/size + (result.getTotal() % size == 0 ? 0 : 1));
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
