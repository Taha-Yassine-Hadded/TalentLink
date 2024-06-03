package com.platform.platformOffreM.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.persistence.*;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ENTREPRISE")
public class Entreprise extends User {

    private String website;

    private String specialisation;

    private String pays;

    @OneToMany(mappedBy = "entreprise", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OffreDeMission> offreDeMissions = new HashSet<>();


    @OneToMany(mappedBy = "entreprise", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Follow> followers = new HashSet<>();


    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Notification> notifications = new HashSet<>();



}
