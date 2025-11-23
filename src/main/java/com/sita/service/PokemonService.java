package com.sita.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sita.dto.PagedResult;
import com.sita.dto.PokemonDto;
import com.sita.model.OwnedPokemon;
import com.sita.model.User;
import com.sita.repository.OwnedPokemonRepository;
import com.sita.repository.PokemonJsonRepository;
import com.sita.repository.UserRepository;

@Service
public class PokemonService {

    private final PokemonJsonRepository pokemonRepository;
    private final UserRepository userRepository;
    private final OwnedPokemonRepository ownedRepository;
    
    public PokemonService(PokemonJsonRepository repository,
    		UserRepository userRepository,
    		OwnedPokemonRepository ownedRepository) {
        this.pokemonRepository = repository;  
        this.userRepository = userRepository;
        this.ownedRepository = ownedRepository;
    }

    public List<PokemonDto> getAll() {
        return pokemonRepository.getAllList();
    }
    
    public PagedResult<PokemonDto> getFiltered(
            String name,
            String type,
            int page,
            int size) {

        Stream<PokemonDto> stream = pokemonRepository.getAllList().stream();

        
        
        
        
//        if (name != null && !name.isBlank()) {
//            String lower = name.toLowerCase();
//
//            stream = stream.filter(p -> {
//                String eng = p.getName().getEnglish();
//                System.out.println("Checking Pokémon: " + eng);
//
//                boolean match = eng != null && eng.toLowerCase().contains(lower);
//                if (match) {
//                    System.out.println("MATCH for: " + eng);
//                }
//                return match;
//            });
//        }

        
        
        
        
        // Filter by name (english)
        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            stream = stream.filter(p ->
                p.getName().getEnglish().toLowerCase().contains(lower)
            );
        }

        // Filter by type
        if (type != null && !type.isBlank()) {
            stream = stream.filter(p ->
                p.getType().stream()
                      .anyMatch(t -> t.equalsIgnoreCase(type))
            );
        }

        List<PokemonDto> filtered = stream.toList();
        int total = filtered.size();

        int from = page * size;
        int to = Math.min(from + size, total);

        List<PokemonDto> items =
                (from >= total) ? List.of() : filtered.subList(from, to);

        return new PagedResult<>(page, size, total, items);
    }

    
    public PagedResult<PokemonDto> getPaged(int page, int size) {

        List<PokemonDto> all = pokemonRepository.getAllList();

        int total = all.size();
        int from = page * size;
        int to = Math.min(from + size, total);

        if (from >= total) {
            return new PagedResult<>(page, size, total, Collections.emptyList());
        }

        List<PokemonDto> slice = all.subList(from, to);

        return new PagedResult<>(page, size, total, slice);
    }


    public User getUser(String userEmail) {
    	return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
    }
    
	public List<PokemonDto> getUserCollection(Long userId) {
		List<Integer> ownedIds = ownedRepository.findByUserId(userId)
                .stream()
                .map(OwnedPokemon::getPokemonId)
                .toList();
		
		return ownedIds.stream()
                .map(pokemonRepository.getAllMap()::get)     
                .filter(Objects::nonNull)   
                .toList();
	}
	
	public String addPokemonToUser(Long userId, Integer pokemonId) {

        if (!userRepository.existsById(userId)) {
            return "User not found";
        }

        Map<Integer, PokemonDto> pokemonMap = pokemonRepository.getAllMap();
        if (!pokemonMap.containsKey(pokemonId)) {
            return "Pokemon not found";
        }

        if (ownedRepository.existsByUserIdAndPokemonId(userId, pokemonId)) {
            return "User already owns this Pokémon";
        }

        OwnedPokemon op = new OwnedPokemon(userId, pokemonId);
        ownedRepository.save(op);

        return "Pokemon added successfully";
    }
	
	@Transactional
	public String removePokemonFromUser(Long userId, Integer pokemonId) {

	    if (!ownedRepository.existsByUserIdAndPokemonId(userId, pokemonId)) {
	        return "User does not own this Pokémon";
	    }

	    ownedRepository.deleteByUserIdAndPokemonId(userId, pokemonId);

	    return "Pokemon removed successfully";
	}

	public PokemonDto getPokemon(int id) {
		PokemonDto pokemon = pokemonRepository.getAllMap().get(id);
		return pokemon;
	}

	

}

