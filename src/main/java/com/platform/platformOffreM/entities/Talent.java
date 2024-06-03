package com.platform.platformOffreM.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("TALENT")
public class Talent extends User {

    @Size(min = 3, message = "Au moins 3 lettres")
    private String prenom;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;

    private String genre;

    @OneToOne(mappedBy = "talent", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Cv cv;

    @OneToMany(mappedBy = "talent")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Candidature> candidatures = new HashSet<>();

    private String profilePicture;


    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Notification> notifications = new HashSet<>();

}
