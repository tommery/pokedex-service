package com.sita.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.sita.dto.PokemonDto;
import com.sita.model.OwnedPokemonEntity;
import com.sita.model.User;
import com.sita.repository.OwnedPokemonRepository;
import com.sita.repository.PokemonJsonRepository;
import com.sita.repository.UserRepository;

@Service
public class PokemonService {

    private final PokemonJsonRepository repository;
    private final UserRepository userRepository;
    private final OwnedPokemonRepository ownedRepository;
    
    public PokemonService(PokemonJsonRepository repository,
    		UserRepository userRepository,
    		OwnedPokemonRepository ownedRepository) {
        this.repository = repository;  
        this.userRepository = userRepository;
        this.ownedRepository = ownedRepository;
    }

    public List<PokemonDto> getAll() {
        return repository.getAllList();
    }
    
    /*public List<PokemonDto> getPokemons() {
    	return repository.getAll();
    }*/

	public List<PokemonDto> getUserCollection(String userEmail) {
		User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
		
		List<Integer> ownedIds = ownedRepository.findByUserId(user.getId())
                .stream()
                .map(OwnedPokemonEntity::getPokemonId)
                .toList();
		
		return ownedIds.stream()
                .map(repository.getAllMap()::get)     
                .filter(Objects::nonNull)   
                .toList();
	}
}

