package fr.univlyon1.m1if.m1if03.dtos;

import fr.univlyon1.m1if.m1if03.classes.Salle;

import java.util.Objects;

public class SalleDTO {

    private String nomSalle;
    private int capacite;
    private int presents;
    private boolean saturee;

    public SalleDTO(){}

    public SalleDTO(Salle salle){
        this.nomSalle = salle.getNom();
        this.capacite = salle.getCapacite();
        this.presents = salle.getPresents();
        this.saturee = salle.getSaturee();
    }

    public String getNomSalle(){
        return this.nomSalle;
    }

    public int getCapacite(){
        return this.capacite;
    }

    public int getPresents(){
        return this.presents;
    }

    public boolean getSaturee() {
        return capacite > -1 && this.getPresents() > this.getCapacite();
    }



    public void setNomSalle(String nomSalle){
        this.nomSalle = nomSalle;
    }

    public void setCapacite(int capacite){
        this.capacite = capacite;
    }

    public void setPresents(int presents){
        this.presents = presents;
    }

    public void setSaturee(boolean saturee){
        this.saturee = saturee;
    }



    public void incPresent() {
        this.presents++;
    }

    public void decPresent() {
        this.presents--;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalleDTO salleDTO = (SalleDTO) o;
        return Objects.equals(nomSalle, salleDTO.nomSalle);
    }
}
