package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.Salle;

import java.util.ArrayList;
import java.util.Collection;

public class SallesDTO extends ArrayList<String> {

    public SallesDTO(Collection<Salle> salles, String url){

        //Affichage des salles
        for (Salle salle: salles){
            add(url.concat("/salles/")
                    .concat(salle.getNom()));
        }

    }
}
