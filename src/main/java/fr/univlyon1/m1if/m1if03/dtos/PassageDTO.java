package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.Passage;

public class PassageDTO {

    private String user;
    private String salle;
    private String dateEntree;
    private String dateSortie;

    public PassageDTO(){}

    public PassageDTO(Passage passage){
        this.user = passage.getUser().getLogin();
        this.salle = passage.getSalle().getNom();
        this.dateEntree = passage.getEntree().toString();
        this.dateSortie = passage.getSortie().toString();
    }

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

    public void setUser(String user) {
        this.user = user;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public void setDateEntree(String dateEntree) {
        this.dateEntree = dateEntree;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
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
