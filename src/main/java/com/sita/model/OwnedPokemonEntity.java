package com.sita.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "owned_pokemon")
public class OwnedPokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pokemon_id", nullable = false)
    private int pokemonId;

    public OwnedPokemonEntity() {}

    public OwnedPokemonEntity(Long userId, int pokemonId) {
        this.userId = userId;
        this.pokemonId = pokemonId;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public int getPokemonId() { return pokemonId; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setPokemonId(int pokemonId) { this.pokemonId = pokemonId; }
}
