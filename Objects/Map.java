package Objects;
import Interfaces.IWorldMap;
import java.util.*;

public class Map implements IWorldMap {
    private int day;
    private final int height;
    private final int width;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private Vector2d jungleStartPoint;
    private int jungleHeight;
    private int jungleWidth;
    private final double jungleRatio; //jungleRatio - length of appropriate jungle edge to length of appropriate map edge
    private HashMap<Vector2d, ArrayList<Animal>> animalHashMap = new HashMap<Vector2d, ArrayList<Animal>>();
    private HashMap<Vector2d, Grass> grassHashMap = new HashMap<>();
    private double numberOfAnimals;
    private double numberOfGrasses;
    private double sumOfAnimalsEnergy;
    private double sumOfDeathsAge;
    private double numberOfDeaths;
    private double sumOfChildren;
    private int[] numberOfDominantGenotypes;
    private int numberOfOffspringOfTrackedAnimal;
    private boolean areAllAnimalsDead;

    public Map(int height, int width, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio) {
        this.day = 0;
        this.height = height;
        this.width = width;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        setJungle();
        this.numberOfOffspringOfTrackedAnimal = 0;
        this.areAllAnimalsDead = false;
    }

    // Getters
    public HashMap<Vector2d, ArrayList<Animal>> getAnimalHashMap() {
        return animalHashMap;
    }

    public int getStartEnergy() {
        return this.startEnergy;
    }

    public int getNumberOfAnimals() {
        return (int) numberOfAnimals;
    }

    public int getNumberOfGrasses() {
        return (int) numberOfGrasses;
    }

    public int getDay() {
        return day;
    }

    public double getAverageEnergy(){
        return (double) Math.round((sumOfAnimalsEnergy / numberOfAnimals) * 100) / 100;
    }

    public double getAverageAgeOfDeaths(){
        if(numberOfDeaths != 0){
            return (double) Math.round((sumOfDeathsAge / numberOfDeaths)* 100) / 100;
        }
        else{
            return 0;
        }
    }

    public double getAverageNumberOfChildren(){

        return (double) Math.round((sumOfChildren / numberOfAnimals) * 100) / 100;
    }

    public int getDominantGenotype(){
        int indexOfMax = -1;
        int maxValue = 0;
        if(numberOfDominantGenotypes != null){
            for(int i = 0; i < this.numberOfDominantGenotypes.length; i++){
                if(numberOfDominantGenotypes[i] > maxValue){
                    maxValue = numberOfDominantGenotypes[i];
                    indexOfMax = i;
                }
            }
            return indexOfMax;
        }
        else{
            return 0;
        }
    }

    public int getNumberOfOffspringOfTrackedAnimal() {
        return numberOfOffspringOfTrackedAnimal;
    }

    public boolean areAllAnimalsDead() {
        return areAllAnimalsDead;
    }

    // Setters
    public void setSumOfAnimalsEnergy(double sumOfAnimalsEnergy) {
        this.sumOfAnimalsEnergy = sumOfAnimalsEnergy;
    }

    public void setSumOfChildren(int sumOfChildren) {
        this.sumOfChildren = sumOfChildren;
    }

    public void setNumberOfDominantGenotypes(int[] numberOfDominantGenotypes) {
        this.numberOfDominantGenotypes = numberOfDominantGenotypes;
    }

    private void setJungle() {
        this.jungleHeight = (int) (this.height * jungleRatio);
        this.jungleWidth = (int) (this.width * jungleRatio);
        int x = (int) ((this.width / 2) - (this.jungleWidth / 2));
        int y = (int) ((this.height / 2) - (this.jungleHeight / 2));
        this.jungleStartPoint = new Vector2d(x, y);
    }

    public void placeNAnimalsOnMap(int n){
        Random rand = new Random();
        int x;
        int y;
        for(int i = 0; i < n; i++){
            do {
                x = rand.nextInt(width);
                y = rand.nextInt(height);

            } while (isOccupied(new Vector2d(x, y)));
            Animal newAnimal = new Animal(this, new Vector2d(x, y));
            this.place(newAnimal);
        }
    }

    public void nextDay() {
        this.day++;
        moveAll();
        eatGrasses();
        removeDeads();
        reproduceAnimals();
        placeNewGrasses();
    }

    public void moveAll(){
        ArrayList<ArrayList<Animal>> animalsCopy = new ArrayList<>(animalHashMap.values());
        for (ArrayList<Animal> animaList : animalsCopy) {
            ArrayList<Animal> animalListCopy = new ArrayList<>(animaList);
            for (Animal animal : animalListCopy) {
                animal.changeOrientation();
                Vector2d oldPosition = animal.getPosition();
                move(animal);
                Vector2d newPosition = animal.getPosition();
                animal.notifyPositionChanged(oldPosition, newPosition);
                animal.changeEnergy(-this.moveEnergy);
            }
        }
    }

    public void move(Animal animal) {
        animal.setPosition(convertPositionToMap(animal.getPosition().add(animal.getOrientation().toUnitVector())));
    }

    // converts position to map when we go outside it
    private Vector2d convertPositionToMap(Vector2d position) {
        int x = position.x < 0 ? this.width - 1 : position.x;
        int y = position.y < 0 ? this.height - 1 : position.y;
        x = x % (this.width);
        y = y % (this.height);
        return new Vector2d(x, y);
    }

    public void eatGrasses(){
        ArrayList<ArrayList<Animal>> animalsCopy = new ArrayList<>(animalHashMap.values());
        for (ArrayList<Animal> animalsAtPosition : animalsCopy) {
            ArrayList<Animal> twoStrongest = Animal.getTwoStrongest(animalsAtPosition);
            Vector2d position = animalsAtPosition.get(0).getPosition();
            if (grassHashMap.containsKey(position) && animalsAtPosition.size() > 0) {
                if (animalsAtPosition.size() > 1 && twoStrongest.get(0) == twoStrongest.get(1)) {
                    twoStrongest.get(0).changeEnergy(grassHashMap.get(position).getPlantEnergy() * 0.5);
                    twoStrongest.get(1).changeEnergy(grassHashMap.get(position).getPlantEnergy() * 0.5);
                } else {
                    twoStrongest.get(0).changeEnergy(grassHashMap.get(position).getPlantEnergy());
                }
                numberOfGrasses--;
                grassHashMap.remove(position);
            }
        }
    }

    public void removeDeads(){
        double _sumOfAnimalsEnergy = 0;
        int _sumOfChildren = 0;
        int[] _numberOfDominantGenotypes = new int[8];
        ArrayList<ArrayList<Animal>> animalsCopy = new ArrayList<>(animalHashMap.values());
        for (ArrayList<Animal> animaList : animalsCopy) {
            ArrayList<Animal> animalListCopy = new ArrayList<>(animaList);
            for (Animal animal : animalListCopy) {
                if (animal.getEnergy() <= 0) {
                    sumOfDeathsAge += this.day - animal.getDayOfBirth();
                    numberOfDeaths++;
                    removeFromAnimalHashMap(animal.getPosition(), animal);
                    animal.setDead();
                    if(numberOfAnimals <= 0){
                        this.areAllAnimalsDead = true;
                    }
                }
                else{
                    _sumOfAnimalsEnergy += animal.getEnergy();
                    _sumOfChildren += animal.getNumberOfChildren();
                    ArrayList<Integer> animalDominantGenotype = animal.getDominantGenotypes();
                    for(int i = 0; i < animalDominantGenotype.size(); i++){
                        _numberOfDominantGenotypes[animalDominantGenotype.get(i)]++;
                    }
                }
            }
        }
        setSumOfAnimalsEnergy(_sumOfAnimalsEnergy);
        setSumOfChildren(_sumOfChildren);
        setNumberOfDominantGenotypes(_numberOfDominantGenotypes);
    }

    public void reproduceAnimals(){
        ArrayList<ArrayList<Animal>> animalsCopy = new ArrayList<>(animalHashMap.values());
        for (ArrayList<Animal> animalsAtPosition : animalsCopy) {
            ArrayList<Animal> twoStrongest = Animal.getTwoStrongest(animalsAtPosition);
            if (animalsAtPosition.size() > 1) {
                Animal newAnimal = Animal.reproduce(twoStrongest.get(0), twoStrongest.get(1));
                if (newAnimal != null) {
                    if(newAnimal.isOffspringOfTrackedAnimal()){
                        numberOfOffspringOfTrackedAnimal++;
                    }
                    placeNewAnimal(twoStrongest.get(0).getPosition(), newAnimal);
                }
            }
        }
    }

    private void placeNewAnimal(Vector2d parentPosition, Animal newAnimal) {
        if (!areAllAroundOccupied(parentPosition)) {
            do {
                newAnimal.setPosition(parentPosition);
                newAnimal.changeOrientation();
                move(newAnimal);
            } while (isOccupied(newAnimal.getPosition()));
        } else {
            newAnimal.setPosition(parentPosition);
            newAnimal.changeOrientation();
            move(newAnimal);
        }
        putToAnimalHashMap(newAnimal.getPosition(), newAnimal);
    }

    public boolean areAllAroundOccupied(Vector2d position) {
        Animal testAnimal = new Animal(this);
        for (int i = 0; i < 8; i++) {
            testAnimal.setPosition(position);
            testAnimal.setOrientation(MapDirection.getDirectionFromValue(i));
            move(testAnimal);
            if (!isOccupied(testAnimal.getPosition())) {
                return false;
            }
        }
        return true;
    }

    private void placeNewGrasses() {
        Random rand = new Random();
        int x;
        int y;
        // place one grass in jungle
        if(!jungleIsFull()) {
            do {
                x = rand.nextInt(jungleWidth);
                y = rand.nextInt(jungleHeight);
                x += jungleStartPoint.x;
                y += jungleStartPoint.y;
            } while (isOccupied(new Vector2d(x, y)));
            Grass jungleGrass = new Grass(plantEnergy);
            numberOfGrasses++;
            grassHashMap.put(new Vector2d(x, y), jungleGrass);
        }
        // place one grass in normal area
        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
        } while (isOccupied(new Vector2d(x, y)) || isOnJungle(x, y));
        Grass normalGrass = new Grass(plantEnergy);
        numberOfGrasses++;
        grassHashMap.put(new Vector2d(x, y), normalGrass);
    }

    public boolean isOnJungle(int x, int y) {
        return jungleStartPoint.x <= x && x < jungleStartPoint.x + this.jungleWidth
                && jungleStartPoint.y <= y && y < jungleStartPoint.y + this.jungleHeight;
    }

    public boolean jungleIsFull(){
        for(int x = jungleStartPoint.x; x < jungleStartPoint.x + jungleWidth; x++){
            for(int y = jungleStartPoint.y; y < jungleStartPoint.y + jungleHeight; y++){
                if(!isOccupied(new Vector2d(x, y))){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        if (!isOccupied(animal.getPosition())) {
            putToAnimalHashMap(animal.getPosition(), animal);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalHashMap.get(position) != null || grassHashMap.get(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        ArrayList<Animal> animalArrayList = animalHashMap.get(position);
        if (animalArrayList != null) {
            return animalArrayList.get(0);
        } else {
            return grassHashMap.get(position);
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        removeFromAnimalHashMap(oldPosition, animal);
        putToAnimalHashMap(newPosition, animal);
    }

    // resets offspring flag when the animal stops being tracked
    public void removeOffspringOfTrackedAnimal(){
        this.numberOfOffspringOfTrackedAnimal = 0;
        ArrayList<ArrayList<Animal>> animalsCopy = new ArrayList<>(animalHashMap.values());
        for (ArrayList<Animal> animaList : animalsCopy) {
            ArrayList<Animal> animalListCopy = new ArrayList<>(animaList);
            for (Animal animal : animalListCopy) {
                animal.setIsOffspringOfTrackedAnimal(false);
            }
        }
    }

    // HashMap Of Arrays - Service
    public void putToAnimalHashMap(Vector2d position, Animal animal) {
        numberOfAnimals++;
        ArrayList<Animal> list = animalHashMap.get(position);
        if (list == null) {
            list = new ArrayList<Animal>();
            animalHashMap.put(position, list);
        }
        list.add(animal);
    }

    public void removeFromAnimalHashMap(Vector2d position, Animal animal) {
        numberOfAnimals--;
        ArrayList<Animal> list = animalHashMap.get(position);
        if (list != null) {
            list.remove(animal);
            animalHashMap.put(position, list);
            if (list.isEmpty()) {
                animalHashMap.remove(position);
            }
        }
    }
}