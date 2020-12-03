package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.User;

import java.util.ArrayList;
import java.util.Collection;

public class UsersDTO extends ArrayList<String> {

    public UsersDTO(Collection<User> users, String url){

        //Affichage des salles
        for (User user: users){
            add(url.concat("/users/")
                    .concat(user.getLogin()));
        }

    }

}
