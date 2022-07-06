package com.ead.authuser.models;

import com.ead.authuser.enums.EUserStatus;
import com.ead.authuser.enums.EUserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //a nível de classe, ao serializar em JSON, ocultará atributos nulos
@Entity
@Table(name = "TB_USERS")
public class UserModel implements Serializable { //Serializable converte numa sequencia de bytes para salvar no BD

    private static final long serialVersionUID = 1L; //id para a JVM vincular com a classe e controlar o versionamento no BD

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId; //id temporal, pois são bases por serviços, chance minima de ter o mesmo id em bases diferentes

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EUserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EUserType userType;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String cpf;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    private void setCreationAndLastUpdateDate() {
        creationDate = LocalDateTime.now(ZoneId.of("UTC"));
        lastUpdateDate = LocalDateTime.now(ZoneId.of("UTC"));
    }

    @PreUpdate
    private void setLastUpdateDate() {
        lastUpdateDate = LocalDateTime.now(ZoneId.of("UTC"));
    }
}
