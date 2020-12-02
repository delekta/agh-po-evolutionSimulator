package Classes;

import Interfaces.IWorldMap;
import Visualizer.MapVisualizer;

import java.util.*;

public class Map implements IWorldMap {
    private int height;
    private int width;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private Vector2d jungleStartPoint;
    private int jungleHeight;
    private int jungleWidth;

    //jungleRatio - length of appropriate jungle edge to length of appropriate map edge
    private double jungleRatio;

    // HashMap Of Arrays
    public LinkedHashMap<Vector2d, ArrayList<Animal>> animalHashMap = new LinkedHashMap<>();

    public LinkedHashMap<Vector2d, Grass> grassHashMap = new LinkedHashMap<>();

    public Map(int height, int width, int startEnergy, int moveEnergy, int plantEnergy, int jungleRatio){
        this.height = height;
        this.width = width;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;

        setJungle();

    }

    public int getStartEnergy() {
        return startEnergy;
    }

    private void setJungle(){
        this.jungleHeight = (int) (this.height * jungleRatio);
        this.jungleWidth = (int) (this.width * jungleRatio);


        int x = (int) ((this.width / 2) - (this.jungleWidth / 2) );
        int y = (int) ((this.height / 2) - (this.jungleHeight / 2) );
        this.jungleStartPoint = new Vector2d(x, y);
    }

    public void run() {

        ArrayList<ArrayList> animalsArray = returnArray(animalHashMap);

        ListIterator<ArrayList> animalsArrayListIterator = animalsArray.listIterator();

        while (animalsArrayListIterator.hasNext()) {
            ArrayList<Animal> animalsAtPosition = animalsArrayListIterator.next();
            ListIterator<Animal> animalListIterator = animalsAtPosition.listIterator();
            while (animalListIterator.hasNext()) {
                Animal animal = animalListIterator.next();

                animal.changeEnergy(-this.moveEnergy);
                if (animal.getEnergy() < 0) {
                    removeFromHashMap(animalHashMap, animal.getPosition(), animal);
                }
                animal.changeOrientation();
                //zmiane zawartosci w hashmapie
                Vector2d oldPosition = animal.getPosition();
                move(animal);
                Vector2d newPosition = animal.getPosition();

                // [] zastanow się czy dobrze rozumiesz Observer!!!
                animal.notifyPositionChanged(oldPosition, newPosition);
            }
        }


        ArrayList<ArrayList> animalsUpdatedArray = returnArray(animalHashMap);

        animalsArrayListIterator = animalsUpdatedArray.listIterator();

        while (animalsArrayListIterator.hasNext()) {
            ArrayList<Animal> animalsAtPosition = animalsArrayListIterator.next();
            if(animalsAtPosition.size() > 1){
                ArrayList<Animal> twoStrongest = Animal.getTwoStrongest(animalsAtPosition);
                Animal newAnimal = Animal.reproduce(twoStrongest.get(0), twoStrongest.get(1));
                // Parents can reproduce when their enegy is too low, then i return null
                if(newAnimal != null){
                    placeNewAnimal(twoStrongest.get(0).getPosition(), newAnimal);
                }
            }


        }
    }

    public void move(Animal animal){
        animal.setPosition(convertPositionToMap(animal.getPosition().add(animal.getOrientation().toUnitVector())));
    }

    private void placeNewAnimal(Vector2d parentPosition, Animal newAnimal){
        if(!areAllAroundOccupied(parentPosition)){
            do{
                newAnimal.setPosition(parentPosition);
                newAnimal.changeOrientation();
                move(newAnimal);
            }while(isOccupied(newAnimal.getPosition()));
        }else{
            newAnimal.setPosition(parentPosition);
            newAnimal.changeOrientation();
            move(newAnimal);
        }
        putToHashMap(animalHashMap, newAnimal.getPosition(), newAnimal);

    }

    public boolean areAllAroundOccupied(Vector2d position){
        Animal testAnimal = new Animal(this);
        for(int i = 0; i < 8; i++){
            testAnimal.setPosition(position);
            testAnimal.setOrientation(MapDirection.getDirectionFromValue(i));
            move(testAnimal);
            if(!isOccupied(testAnimal.getPosition())){
                return false;
            }
        }
        return true;
    }



    // converts position to map when we go outside it
    private Vector2d convertPositionToMap(Vector2d position){
        return new Vector2d(position.x % this.width, position.y % this.height);
    }


    @Override
    public String toString() {
        MapVisualizer visual = new MapVisualizer(this);
        return visual.draw(new Vector2d(0, 0), new Vector2d(this.width, this.height));
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        removeFromHashMap(animalHashMap, oldPosition, animal);
        putToHashMap(animalHashMap, newPosition, animal);
    }


    private void placeGrasses(int number){
        Random rand = new Random();
        int x;
        int y;
        // place grass in jungle
        do{
            x = rand.nextInt(jungleWidth + 1);
            y = rand.nextInt(jungleHeight + 1);

        }while(isOccupied(new Vector2d(x, y)));
        Grass jungleGrass = new Grass(plantEnergy);
        grassHashMap.put(new Vector2d(x, y), jungleGrass);

        // place grass in normal area
        do{
            x = rand.nextInt(width);
            y = rand.nextInt(height);

        }while(isOccupied(new Vector2d(x, y)) || isOnJungle(x, y));

        Grass normalGrass = new Grass(plantEnergy);
        grassHashMap.put(new Vector2d(x, y), normalGrass);

    }

    // [] check if e.g. jungleStartPoint.x + width is included
    private boolean isOnJungle(int x, int y){
        return   jungleStartPoint.x < x && x <= jungleStartPoint.x + width
                && jungleStartPoint.y < y && y <= jungleStartPoint.y + height;
    }

    @Override
    public boolean place(Animal animal) {
        if(!isOccupied(animal.getPosition())) {
            putToHashMap(animalHashMap, animal.getPosition(), animal);
            return true;
        }
        return false;
        // [] Narazie zakładam że nie możemy postawić ani na trawie ani na innym zwierzęciu
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return animalHashMap.get(position) != null || grassHashMap.get(position) != null;
    }


    @Override
    public Object objectAt(Vector2d position) {
        ArrayList<Animal> animalArrayList = animalHashMap.get(position);
        if(animalArrayList != null){
            // [] Do zastanowienia czy np nie chcemy wypisywać tego z największą energią
            return animalArrayList.get(0);
        }
        else{
            // If there is no grass - returns null
            return grassHashMap.get(position);
        }
    }

    // HashMap Of Arrays - Service
    public static void putToHashMap(LinkedHashMap hashMap, Vector2d position, Animal animal){

        ArrayList<Animal> list = (ArrayList<Animal>) hashMap.get(position);
        if(list == null){
            list = new ArrayList<Animal>();
            hashMap.put(position, list);
        }
        list.add(animal);
    }

    public static void removeFromHashMap(LinkedHashMap hashMap, Vector2d position, Animal animal){
        ArrayList<Animal> list = (ArrayList<Animal>) hashMap.get(position);
        if(list != null){
            list.remove(animal);
        }
        if(list.isEmpty()){
            hashMap.remove(position);
        }
    }

    public static ArrayList<ArrayList> returnArray(LinkedHashMap hashMap){
        return new ArrayList<ArrayList>( hashMap.values() );
    }
}