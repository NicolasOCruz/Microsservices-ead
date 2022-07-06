package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {


    /*  JSON View representa os tipos de visualização que a classe vai ter.
        No caso desse DTO, a classe será usada para múltiplas operações.
        Para evitar a criação de um DTO específico para cada operação,
        o JSON View limita quais atributos da classe serão vistos/usados em cada operação.
        A interface abaixo lista as 4 operações possíveis, e em cada atributo anota-se
        em qual(is) visão(ões) (operações) ele será visível.
    */

    public interface UserView {
        public static interface RegistrationPost {}
        public static interface UserPut {}
        public static interface PasswordPut {}
        public static interface ImagePut {}
    }

    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView(UserView.RegistrationPost.class)
    private String cpf;

    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
