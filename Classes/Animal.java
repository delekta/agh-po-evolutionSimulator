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
    private int startEnergy = map.getStartEnergy();

    public int getStartEnergy() {
        return startEnergy;
    }

    private double energy = this.getStartEnergy();

    public double getEnergy() {
        return energy;
    }

    public void changeEnergy(double changeValue){
        this.energy += changeValue;
    }
    public void setEnergy(double energy){
        this.energy = energy;
    }


    private int[] genes = setRandomGenes();

    public int[] getGenes() {
        return genes;
    }

    public void changeOrientation(){
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

    // Function returns to strongest animals at one position
    // When there is one animal, it returns one animal, it is useful in eating
    public static ArrayList<Animal> getTwoStrongest(ArrayList<Animal> animals){

        // I callout this funcyion only when this condition is true
        if(animals.size() > 1){
            Animal animal1 = animals.get(1);
            Animal animal2 = animals.get(0);

            // animal1 is bigger than animal2
            if(animals.get(0).getEnergy() >= animals.get(1).getEnergy()){
                animal1 = animals.get(0);
                animal2 = animals.get(1);
            }

            for(int i = 2; i < animals.size(); i++){
                if(animals.get(i).getEnergy() > animal1.getEnergy()){
                    animal2 = animal1;
                    animal1 = animals.get(i);
                }else if(animals.get(i).getEnergy() > animal2.getEnergy()){
                    animal2 = animals.get(i);
                }
            }
            ArrayList res = new ArrayList<Animal>();
            res.add(animal1);
            res.add(animal2);
            return res;
        }else if(animals.size() == 1){
            return animals;
        }
        return null;
    }

    public static Animal reproduce(Animal parent1, Animal parent2){
        if(parent1.getEnergy() >= 0.5 * parent1.getStartEnergy()
           && parent2.getEnergy() >= 0.5 * parent2.getStartEnergy()){
            parent1.changeEnergy(-0.5 * parent1.getEnergy());
            parent2.changeEnergy(-0.5 * parent2.getEnergy());

            int[] childGenes;
            childGenes = parent1.getGenesFromParents(parent1, parent2);


            Animal child = new Animal(parent1.map, parent1.getPosition(), childGenes);
            // wiec dziecko dostaje taką energie, nie energie startową?
            child.setEnergy(0.5 * parent1.getEnergy() + 0.5 * parent2.getEnergy());

            return child;
        }
        return null;
    }

    private int[] getGenesFromParents(Animal parent1, Animal parent2){
        int[] childGenes = new int[32];
        Random random = new Random();
        int firstBreakPoint;
        int secondBreakPoint;
        do{
            firstBreakPoint = random.nextInt(32);
            secondBreakPoint = random.nextInt(32);
        }while(firstBreakPoint < secondBreakPoint);
        for(int i = 0; i < 32; i++){
            if(i < firstBreakPoint || i >= secondBreakPoint){
                childGenes[i] = parent1.getGenes()[i];
            }
            else {
                childGenes[i] = parent2.getGenes()[i];
            }
        }
        Arrays.sort(childGenes);
        // Checking if genes conatins all orientations
        int currentVal = childGenes[0];
        int expectedVal = 0;

        int i = 0;
        while(i < 32){
            currentVal = childGenes[i];
            if(currentVal == expectedVal){
                expectedVal++;
            }
            else{
                int idxToChange = random.nextInt(32);
                childGenes[idxToChange] = expectedVal;
                Arrays.sort(childGenes);
                i = 0;
                currentVal = childGenes[0];
                expectedVal = 0;
            }
            i++;
            while( i < 32 && childGenes[i] == currentVal){
                i++;
            }
        }
        return childGenes;
    }

    private void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    private void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public void notifyPositionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver o: observers){
            o.positionChanged(oldPosition, newPosition, this);
        }
    }


}
