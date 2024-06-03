package com.platform.platformOffreM.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.platformOffreM.entities.CvInfos.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String introduction;


    @OneToMany(mappedBy = "cv", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "cv", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Competence> competences = new HashSet<>();

    @OneToMany(mappedBy = "cv", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Experience> experiences = new HashSet<>();

    @OneToMany(mappedBy = "cv", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Formation> formations = new HashSet<>();

    @OneToMany(mappedBy = "cv", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Langue> langues = new HashSet<>();

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Talent talent;

    private String cvPicture;

}
