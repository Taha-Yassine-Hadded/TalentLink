package com.platform.platformOffreM.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Talent talent;

    @ManyToOne(fetch = FetchType.EAGER)
    private Entreprise entreprise;

    @ManyToOne(fetch = FetchType.EAGER)
    private OffreDeMission offreDeMission;

    private String message;

    private LocalDateTime date;

    private boolean seen;
}
