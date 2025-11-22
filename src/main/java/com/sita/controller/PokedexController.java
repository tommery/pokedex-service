package com.sita.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sita.dto.AuthRequest;
import com.sita.dto.PokemonDto;
import com.sita.service.AuthService;
import com.sita.service.JwtService;
import com.sita.service.PokemonService;
import com.sita.service.UserPokemonService;

@RestController
@RequestMapping("/api/v1")
public class PokedexController {
	
	private final PokemonService pokemonService;
	private final AuthService authService;
	private final UserPokemonService userPokemonService;
	private final JwtService jwtService;
	
	@Autowired  
	public PokedexController(PokemonService pokemonService,
			AuthService authService,
			UserPokemonService userPokemonService,
			JwtService jwtService) {
        this.pokemonService = pokemonService;
        this.authService = authService;
        this.userPokemonService = userPokemonService;
        this.jwtService = jwtService;
    }
	
	@GetMapping("/hello")
    public String hello(@RequestParam(required = false, defaultValue = "Moshe") String name) {
        return "success take 3";
    }
	
	@GetMapping("/all")
    public List<PokemonDto> getAllPokemons() {
        return pokemonService.getAll();
    }
	
	@GetMapping("/pokemon")
    public PokemonDto getPokemon() {
        return pokemonService.getAll().get(0);
    }
	
	//@PostMapping("/register")
    //public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
	@GetMapping("/register")
	public ResponseEntity<String> register(@RequestParam(required = false, defaultValue = "Moshe") String username,
			@RequestParam(required = true) String email,
			@RequestParam(required = true) String password) {
		
		AuthRequest ar = new AuthRequest();
		ar.setEmail(email);
		ar.setPassword(password);
		ar.setUsername(username);
        boolean ok = authService.register(ar);
        if (ok) return ResponseEntity.ok("Registered");
        return ResponseEntity.badRequest().body("Email already exists");
    }
	
	@GetMapping("/login")
	public String login(@RequestParam String email,
	                               @RequestParam String password) {

	    AuthRequest req = new AuthRequest();
	    req.setEmail(email);
	    req.setPassword(password);

	    Long userId = authService.login(req);
	    if (userId == null) {
	    	return "Invalid credentials"; 
	    }

	    return jwtService.generate(userId);
	}

	@GetMapping("/guest")
	public String guestToken() {
	    return jwtService.generateGuestToken();
	}

}
