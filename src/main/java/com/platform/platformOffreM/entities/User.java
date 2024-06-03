package com.platform.platformOffreM.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 10)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, message = "Au moins 3 lettres")
    private String nom;


    @Size(min = 4, message = "Au moins 4 lettres")
    private String username;

    @Size(min = 8, message = "Au moins 8 lettres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @Email(message = "Email doit etre bien forme")
    private String email;

    private boolean enabled = true;

    @ManyToOne
    private Role role;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

}
