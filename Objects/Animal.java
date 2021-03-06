package Objects;

import Interfaces.IMapElement;
import Interfaces.IPositionChangeObserver;
import Interfaces.IWorldMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Animal implements IMapElement {
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private final IWorldMap map;
    private final int dayOfBirth;
    private int numberOfChildren;
    private ArrayList<Integer> dominantGenotypes;
    private final int[] genes;
    private final int startEnergy;
    private double energy;
    private boolean isDead = false;
    private boolean isOffspringOfTrackedAnimal = false;
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2,2);

    // Constructors
    public Animal(IWorldMap map){
        this.map = map;
        this.dayOfBirth = this.map.getDay();
        this.numberOfChildren = 0;
        this.startEnergy = map.getStartEnergy();
        this.energy = this.getStartEnergy();
        this.genes = getRandomGenes();
        addObserver(map);
        setDominantGenotypes();
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.dayOfBirth = this.map.getDay();
        this.numberOfChildren = 0;
        this.startEnergy = map.getStartEnergy();
        this.energy = this.getStartEnergy();
        this.genes = getRandomGenes();
        addObserver(map);
        this.setPosition(initialPosition);
        setDominantGenotypes();
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int[] genes){
        this.map = map;
        this.dayOfBirth = this.map.getDay();
        this.numberOfChildren = 0;
        this.startEnergy = map.getStartEnergy();
        this.energy = this.getStartEnergy();
        addObserver(map);
        this.setPosition(initialPosition);
        this.genes = genes;
        setDominantGenotypes();
    }

    // Getters
    public MapDirection getOrientation() { return orientation; }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public double getEnergy() {
        return this.energy;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public ArrayList<Integer> getDominantGenotypes() {
        return dominantGenotypes;
    }

    private int[] getRandomGenes(){
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

    public Vector2d getPosition(){return position;}

    public boolean isDead() {
        return isDead;
    }

    public int[] getGenes() {
        return genes;
    }

    public boolean isOffspringOfTrackedAnimal() {
        return isOffspringOfTrackedAnimal;
    }


    // Setters
    public void setIsOffspringOfTrackedAnimal(boolean isOffspringOfTrackedAnimal) {
        this.isOffspringOfTrackedAnimal = isOffspringOfTrackedAnimal;
    }

    public void setPosition(Vector2d v){ this.position = v;}

    public void setDead(){
        this.isDead = true;
    }

    public void setEnergy(double energy){
        this.energy = energy;
    }

    public void setOrientation(MapDirection o){ this.orientation = o;}

    public void changeEnergy(double changeValue){
        this.energy += changeValue;
    }

    public void changeOrientation(){
        Random random = new Random();
        int valueOfTurn = this.genes[random.nextInt(32)];
        int valueOfResult = (this.orientation.getValueOfDirection() + valueOfTurn) % 8;
        this.orientation = MapDirection.getDirectionFromValue(valueOfResult);
    }

    private void setDominantGenotypes(){
        int[] numberOfGenes = new int[8];
        for(int i = 0; i < 32; i++){
            numberOfGenes[this.getGenes()[i]]++;
        }
        int max = 0;
        for(int i = 0; i < 8; i++){
            if(numberOfGenes[i] > max){
                max = numberOfGenes[i];
            }
        }
        ArrayList<Integer> dominantGenotypes = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            if(numberOfGenes[i] == max){
                dominantGenotypes.add(i);
            }
        }
        this.dominantGenotypes = dominantGenotypes;
    }

    public static ArrayList<Animal> getTwoStrongest(ArrayList<Animal> animals){
        if(animals.size() > 1){
            Animal animal1 = animals.get(1);
            Animal animal2 = animals.get(0);

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
            ArrayList<Animal> res = new ArrayList<Animal>();
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
            double childEnergy = 0.25 * parent1.getEnergy() + 0.25 * parent2.getEnergy();
            parent1.changeEnergy(-0.25 * parent1.getEnergy());
            parent2.changeEnergy(-0.25 * parent2.getEnergy());

            parent1.numberOfChildren++;
            parent2.numberOfChildren++;

            int[] childGenes;
            childGenes = parent1.getGenesFromParents(parent1, parent2);
            Animal child = new Animal(parent1.map, parent1.getPosition(), childGenes);
            child.setEnergy(childEnergy);
            child.setIsOffspringOfTrackedAnimal(parent1.isOffspringOfTrackedAnimal || parent2.isOffspringOfTrackedAnimal);
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
        // Checking if genes contains all orientations
        int currentVal;
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

    // Observers Functions
    private void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void notifyPositionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver o: observers){
            o.positionChanged(oldPosition, newPosition, this);
        }
    }
}
