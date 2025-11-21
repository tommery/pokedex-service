package com.sita.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sita.dto.PokemonDto;
import com.sita.repository.PokemonJsonRepository;

@Service
public class PokemonService {

    private final PokemonJsonRepository repository;

    public PokemonService(PokemonJsonRepository repository) {
        this.repository = repository;  // injected automatically
    }

    public List<PokemonDto> getAll() {
        return repository.getAll();
    }
}

