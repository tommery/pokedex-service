package com.sita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sita.model.OwnedPokemon;

public interface OwnedPokemonRepository extends JpaRepository<OwnedPokemon, Long> {

    List<OwnedPokemon> findByUserId(Long userId);

    void deleteByUserIdAndPokemonId(Long userId, Integer pokemonId);

    boolean existsByUserIdAndPokemonId(Long userId, Integer pokemonId);
}