package Classes;

import Interfaces.IMapElement;

public class Grass implements IMapElement {
    private Vector2d position;
    private int energy;
    public Grass(int plantEnergy){
        this.energy = plantEnergy;
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString(){
        return "*";
    }

    public Grass(Vector2d position){
        this.position = position;
    }
}