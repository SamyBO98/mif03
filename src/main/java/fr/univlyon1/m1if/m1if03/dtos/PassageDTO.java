package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.User;

import java.util.Date;
import java.util.Objects;

public class PassageDTO {

    private static int id;

    private String user;
    private String salle;
    private String dateEntree;
    private String dateSortie;

    private static int getCounter() {
        return ++id;
    }

    public PassageDTO() {
        this.id = getCounter();
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getSalle() {
        return salle;
    }

    public String getEntree() {
        return dateEntree;
    }

    public String getSortie() {
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
