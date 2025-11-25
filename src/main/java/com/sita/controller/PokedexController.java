package com.sita.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sita.dto.AuthRequest;
import com.sita.dto.PagedResult;
import com.sita.dto.PokemonDto;
import com.sita.exception.AppException;
import com.sita.logging.AppLogger;
import com.sita.logging.Log;
import com.sita.service.AuthService;
import com.sita.service.JwtService;
import com.sita.service.PokemonService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class PokedexController {
	
	private static final AppLogger log = Log.get(PokedexController.class);
	
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
	
	@GetMapping("/list")
	public ResponseEntity<PagedResult<PokemonDto>> list(
			@RequestParam(required = false) String name,
	        @RequestParam(required = false) String type,
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "20") int size) {

		size = Math.min(size, 100);  // max size = 100
	    size = Math.max(size, 10);   // min size = 10

	    int internalPage = Math.max(0, page - 1);
	    
	    PagedResult<PokemonDto> result =
	            pokemonService.getFiltered(name, type, internalPage, size);
	    
	    return ResponseEntity.ok(result);
	}
	
	@GetMapping("/pokemon/{id}")
	public ResponseEntity<PokemonDto> getPokemon(@PathVariable int id) {
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
        	log.error("Unable to register user. Email {} already exists", request.getEmail());
        	throw new AppException("User with email "+request.getEmail()+" already exists", HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok("Registered");
    }
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody AuthRequest request) {
	    Long userId = authService.login(request);
	    if (userId == null) {
	    	log.error("Failed to login. Invalid credentials");
	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
	    }
	    return ResponseEntity.ok(jwtService.generateToken(userId));
	}

	@GetMapping("/collection")
	public ResponseEntity<List<PokemonDto>> getUserCollection(HttpServletRequest request) {
		Long userId = getUserIdFromHeader(request);
	    List<PokemonDto> list = pokemonService.getUserCollection(userId);
	    return ResponseEntity.ok(list);
	}

	@PostMapping("/collection/{pokemonId}")
    public ResponseEntity<String> addPokemon(HttpServletRequest request,
            @PathVariable Integer pokemonId) {

		Long userId = getUserIdFromHeader(request);
        if (userId<0) {
        	log.error("JWT validation failed for user {}",userId);
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        String result = pokemonService.addPokemonToUser(userId, pokemonId);
        return ResponseEntity.ok(result);
    }
	
	@DeleteMapping("/collection/{pokemonId}")
    public ResponseEntity<String> removePokemon(HttpServletRequest request,
    		@PathVariable Integer pokemonId) {
        
		Long userId = getUserIdFromHeader(request);
        if (userId<0) {
        	log.error("JWT validation failed for user {}",userId);
            return ResponseEntity.status(401).body("Invalid token");
        }

        String result = pokemonService.removePokemonFromUser(userId, pokemonId);
        return ResponseEntity.ok(result);
    }
	
	private Long getUserIdFromHeader(HttpServletRequest request) {
	    String header = request.getHeader("Authorization");
	    if (header == null || !header.startsWith("Bearer ")) {
	    	log.error("Missing or invalid Authorization header");
	        throw new RuntimeException("Missing or invalid Authorization header");
	    }

	    String token = header.substring(7); // remove "Bearer "
	    return extractUserId(token);
	}

	
	private Long extractUserId(String token) {
		if (token == null || token.isBlank()) {
	        throw new RuntimeException("Missing token");
	    }
		
		if (!jwtService.validate(token)) {
			log.error("JWT validation failed for token {}",token);
			return -1L;
		}
		

		try {
			return Long.valueOf(jwtService.extractId(token));
		} catch (NumberFormatException e) {
			return -1L;
		}

	}
	
	

}
