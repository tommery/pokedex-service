package com.sita.dto;

import java.util.List;

public class ProfileDto {

    private String height;
    private String weight;
    private List<String> egg;
    private List<List<String>> ability;
    private String gender;

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public List<String> getEgg() { return egg; }
    public void setEgg(List<String> egg) { this.egg = egg; }

    public List<List<String>> getAbility() { return ability; }
    public void setAbility(List<List<String>> ability) { this.ability = ability; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
