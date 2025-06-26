package org.example.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "country", nullable = false)
    private String name;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private Set<City> cities = new HashSet<>();

    public Country() {}

    public Integer getCountryId() { return countryId; }
    public void setCountryId(Integer countryId) { this.countryId = countryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<City> getCities() { return cities; }
    public void setCities(Set<City> cities) { this.cities = cities; }
}
