package com.platform.platformOffreM.entities.CvInfos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.OffreDeMission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String niveau;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cv cv;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private OffreDeMission offreDeMission;

}
