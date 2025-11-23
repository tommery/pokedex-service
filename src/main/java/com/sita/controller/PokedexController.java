package com.sita.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sita.dto.AuthRequest;
import com.sita.dto.PokemonDto;
import com.sita.model.User;
import com.sita.service.AuthService;
import com.sita.service.JwtService;
import com.sita.service.PokemonService;

@RestController
@RequestMapping("/api/v1")
public class PokedexController {
	
	private final PokemonService pokemonService;
	private final AuthService authService;
	private final JwtService jwtService;
	
	@Autowired  
	public PokedexController(PokemonService pokemonService,
			AuthService authService,
			JwtService jwtService) {
        this.pokemonService = pokemonService;
        this.authService = authService;
        this.jwtService = jwtService;
    }
	
	@GetMapping("/all")
    public List<PokemonDto> getAllPokemons() {
        return pokemonService.getAll();
    }
	
	@GetMapping("/pokemon")
	public ResponseEntity<PokemonDto> getPokemon(@RequestParam int id) {
	    PokemonDto pokemon = pokemonService.getPokemon(id);

	    if (pokemon == null) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok(pokemon);
	}

	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        boolean ok = authService.register(request);
        if (!ok) {
        	return ResponseEntity.badRequest().body("Email already exists");
        }
        return ResponseEntity.ok("Registered");
    }
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody AuthRequest request) {
	    Long userId = authService.login(request);
	    if (userId == null) {
	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
	    }
	    return ResponseEntity.ok(jwtService.generateToken(userId));
	}

	@GetMapping("/guest")
	public String guestToken() {
	    return jwtService.generateGuestToken();
	}
	
	@GetMapping("/collection")
	public ResponseEntity<List<PokemonDto>> getUserCollection(@RequestParam String token) {
		String email = jwtService.extractEmail(token);
	    List<PokemonDto> list = pokemonService.getUserCollection(email);
	    return ResponseEntity.ok(list);
	}

	@GetMapping("/add")
    public ResponseEntity<String> addPokemon(
            @RequestParam String token,
            @RequestParam Integer pokemonId
    ) {

		Long userId = validate(token);
        if (userId<0) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        String result = pokemonService.addPokemonToUser(userId, pokemonId);
        return ResponseEntity.ok(result);
    }
	
	@DeleteMapping("/remove")
    public ResponseEntity<String> removePokemon(
            @RequestParam String token,
            @RequestParam Integer pokemonId) {
        
		Long userId = validate(token);
        if (userId<0) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        String result = pokemonService.removePokemonFromUser(userId, pokemonId);
        return ResponseEntity.ok(result);
    }
	
	private Long validate(String token) {
		if (!jwtService.validate(token)) {
			return -1L;
		}
		
		User user = pokemonService.getUser(jwtService.extractEmail(token));
		
		return user.getId();
	}

}
