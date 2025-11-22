package com.sita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sita.model.OwnedPokemonEntity;

public interface OwnedPokemonRepository extends JpaRepository<OwnedPokemonEntity, Long> {

    List<OwnedPokemonEntity> findByUserId(Long userId);

    void deleteByUserIdAndPokemonId(Long userId, Integer pokemonId);

    boolean existsByUserIdAndPokemonId(Long userId, Integer pokemonId);
}