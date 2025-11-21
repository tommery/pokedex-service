package com.sita.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sita.dto.PokemonDto;

import com.fasterxml.jackson.core.type.TypeReference;

@Repository
public class PokemonJsonRepository {

    private final List<PokemonDto> pokemons;

    public PokemonJsonRepository() {
        this.pokemons = loadFromJson(); // load once at startup
    }

    private List<PokemonDto> loadFromJson() {
    	List<PokemonDto> pokemons = null;
    	System.out.println("PokemonJsonRepository: starting to load JSON...");
    	
    	try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("data/Pokedex.json");

            System.out.println("Resource exists? " + resource.exists());
            System.out.println("Resource path: " + resource.getPath());

            InputStream is = resource.getInputStream();

            pokemons = mapper.readValue(
                    is,
                    new TypeReference<List<PokemonDto>>() {}
            );

            System.out.println("✅ Loaded " + pokemons.size() + " pokemons!");

        } catch (Exception e) {
            System.out.println("ERROR LOADING JSON: " + e.getClass());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load pokedex.json", e);
        }
    	
    	return pokemons;
	}

	public List<PokemonDto> getAll() {
        return pokemons;
    }
}
