package com.sita.repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sita.dto.PokemonDto;

@Repository
public class PokemonJsonRepository {

    private List<PokemonDto> pokemonList;
    private Map<Integer,PokemonDto> pokemonMap;
    
    public PokemonJsonRepository() {
        this.pokemonList = null;
		this.pokemonMap = null;
		loadFromJson(); // load once at startup
    }

    private void loadFromJson() {
        System.out.println("PokemonJsonRepository: starting to load JSON...");

        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("data/Pokedex.json");

            System.out.println("Resource exists? " + resource.exists());
            System.out.println("Resource path: " + resource.getPath());

            InputStream is = resource.getInputStream();

            // טוען את הרשימה מה-JSON
            List<PokemonDto> loadedList = mapper.readValue(
                    is,
                    new TypeReference<List<PokemonDto>>() {}
            );

            System.out.println("✅ Loaded " + loadedList.size() + " pokemons!");

            // בונה list + map בלולאה אחת
            this.pokemonList = new ArrayList<>(loadedList.size());
            this.pokemonMap  = new HashMap<>(loadedList.size());

            for (PokemonDto p : loadedList) {
                pokemonList.add(p);
                pokemonMap.put(p.getId(), p);   // בהרשאה שיש לזה getId()
            }

            System.out.println("Map size: " + pokemonMap.size());

        } catch (Exception e) {
            System.out.println("ERROR LOADING JSON: " + e.getClass());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load Pokedex.json", e);
        }
    }


	public List<PokemonDto> getAllList() {
        return pokemonList;
    }
	
	public Map<Integer, PokemonDto> getAllMap() {
        return pokemonMap;
    }
}
