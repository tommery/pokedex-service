package com.sita.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    /*public List<PokemonDto> getPokemons() {
    	return repository.getAll();
    }*/

    public User getUser(String userEmail) {
    	return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
    }
    
	public List<PokemonDto> getUserCollection(String userEmail) {
		User user = getUser(userEmail);
		
		List<Integer> ownedIds = ownedRepository.findByUserId(user.getId())
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

}

