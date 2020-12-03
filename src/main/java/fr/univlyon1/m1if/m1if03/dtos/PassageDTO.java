package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.User;

import java.util.Date;
import java.util.Objects;

public class PassageDTO {

    private String user;
    private String salle;
    private String dateEntree;
    private String dateSortie;

    public PassageDTO(){}

    public String getUser() {
        return user;
    }

    public String getSalle() {
        return salle;
    }

    public String getDateEntree() {
        return dateEntree;
    }

    public String getDateSortie() {
        return dateSortie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageDTO passage = (PassageDTO) o;
        return user.equals(passage.user) &&
                salle.equals(passage.salle) &&
                dateEntree.equals(passage.dateEntree) &&
                dateSortie.equals(passage.dateSortie);
    }
}
