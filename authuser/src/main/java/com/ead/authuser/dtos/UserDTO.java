package com.ead.authuser.dtos;

import com.ead.authuser.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @UsernameConstraint(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class)
    @Size(min = 6, max = 20, groups = UserView.PasswordPut.class)
    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView(UserView.RegistrationPost.class)
    private String cpf;

    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
