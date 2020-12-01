package Classes;

import Interfaces.IMapElement;
import Interfaces.IPositionChangeObserver;
import Interfaces.IWorldMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Animal implements IMapElement {

    // Added during lab7, IPositionChangeObserver???
    public List<IPositionChangeObserver> observers = new ArrayList<>();

    private IWorldMap map;

    private MapDirection orientation = MapDirection.NORTH;
    public void setOrientation(MapDirection o){ this.orientation = o;}
    public MapDirection getOrientation() { return orientation; }

    private Vector2d position = new Vector2d(2,2);
    public void setPosition(Vector2d v){ this.position = v;}
    public Vector2d getPosition(){return position;}

    // Start Energy powinno byc przeniesione do Mapy???
    private double energy = map.getStartEnergy();

    public double getEnergy() {
        return energy;
    }

    public void changeEnergy(int changeValue){
        this.energy += changeValue;
    }


    private int[] genes = setRandomGenes();

    private void changeOrientation(){
        Random random = new Random();
        int valueOfTurn = this.genes[random.nextInt(32)];
        int valueOfResult = (this.orientation.getValueOfDirection() + valueOfTurn) % 8;
        this.orientation = MapDirection.getDirectionFromValue(valueOfResult);
    }

    public String toString(){
        return String.valueOf(this.energy);
    }

    // Constructors
    public Animal(IWorldMap map){
        this.map = map;
        addObserver(map);
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        addObserver(map);
        this.setPosition(initialPosition);
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int[] genes){
        this.map = map;
        addObserver(map);
        this.setPosition(initialPosition);
        this.genes = genes;

    }

    private int[] setRandomGenes(){
        Random random = new Random();
        int [] genes = new int[32];
        for (int i = 0; i < 32; i++){
            if(i < 8){
                genes[i] = i;
            }
            else{
                genes[i] = random.nextInt(8);
            }
        }
        Arrays.sort(genes);
        return genes;
    }

    public Animal reproduce(){

    }

    private void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    private void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public void notifyPositionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver o: observers){
            o.positionChanged(oldPosition, newPosition);
        }
    }


}
