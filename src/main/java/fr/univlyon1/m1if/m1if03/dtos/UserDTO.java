package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.User;

import java.util.Objects;

public class UserDTO {

    private String login;
    private String nom;
    private boolean admin;

    public UserDTO(){}

    public UserDTO(User user){
        this.login = user.getLogin();
        this.nom = user.getNom();
        this.admin = user.getAdmin();
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getLogin() {
        return login;
    }

    public String getNom() {
        return nom;
    }

    public boolean getAdmin() {
        return admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return admin == userDTO.admin &&
                Objects.equals(login, userDTO.login) &&
                Objects.equals(nom, userDTO.nom);
    }

}
