package com.sita.dto;

import java.util.List;

public class PokemonDto {

    private int id;
    private NameDto name;
    private List<String> type;
    private BaseStatsDto base;
    private String species;
    private String description;
    private EvolutionDto evolution;
    private ProfileDto profile;
    private ImageDto image;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public NameDto getName() { return name; }
    public void setName(NameDto name) { this.name = name; }

    public List<String> getType() { return type; }
    public void setType(List<String> type) { this.type = type; }

    public BaseStatsDto getBase() { return base; }
    public void setBase(BaseStatsDto base) { this.base = base; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public EvolutionDto getEvolution() { return evolution; }
    public void setEvolution(EvolutionDto evolution) { this.evolution = evolution; }

    public ProfileDto getProfile() { return profile; }
    public void setProfile(ProfileDto profile) { this.profile = profile; }

    public ImageDto getImage() { return image; }
    public void setImage(ImageDto image) { this.image = image; }
}

