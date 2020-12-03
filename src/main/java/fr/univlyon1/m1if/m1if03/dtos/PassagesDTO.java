package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.Passage;

import java.util.ArrayList;
import java.util.Collection;

public class PassagesDTO extends ArrayList<String> {

    public PassagesDTO(Collection<Passage> passages, String url){

        for (Passage passage: passages){
            add(url.concat("/passages/")
                    .concat(String.valueOf(passage.getId())));
        }

    }

}
