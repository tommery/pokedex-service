package com.sita.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sita.dto.PokemonDto;
import com.sita.service.AuthService;
import com.sita.service.PokemonService;
import com.sita.service.UserPokemonService;

@RestController
@RequestMapping("/api/v1")
public class PokedexController {
	
	private final PokemonService pokemonService;
	private final AuthService authService;
	private final UserPokemonService userPokemonService;
	
	@Autowired  
	public PokedexController(PokemonService pokemonService,
			AuthService authService,
			UserPokemonService userPokemonService) {
        this.pokemonService = pokemonService;
        this.authService = authService;
        this.userPokemonService = userPokemonService;
    }
	
	@GetMapping("/hello")
    public String hello(@RequestParam(required = false, defaultValue = "Moshe") String name) {
        return "success";
    }
	
	@GetMapping("/all")
    public List<PokemonDto> getAllPokemons() {
        return pokemonService.getAll();
    }
	
}
