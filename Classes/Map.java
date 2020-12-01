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

    @Override
    public void run() {


        ListIterator<Animal> animalListIterator = animals.listIterator();

        for (MoveDirection direction : directions) {
            if (!animalListIterator.hasNext()) {
                animalListIterator = animals.listIterator();
            }
            Animal animal = animalListIterator.next();

//            debugger(animal, direction);

            switch (direction) {
                // losowo ze losujesz orientacje i ruszasz
                case FORWARD:
                    if (animal.getPosition().add(animal.getOrientation().toUnitVector())) {

                        Vector2d oldPosition = animal.getPosition();
                        animal.setPosition(animal.getPosition().add(animal.getOrientation().toUnitVector()));
                        Vector2d newPosition = animal.getPosition();

                        animal.notifyPositionChanged(oldPosition, newPosition);

                    }
                    break;
                case BACKWARD:
                    if (animal.getPosition().subtract(animal.getOrientation().toUnitVector())) {

                        Vector2d oldPosition = animal.getPosition();
                        animal.setPosition(animal.getPosition().subtract(animal.getOrientation().toUnitVector()));
                        Vector2d newPosition = animal.getPosition();

                        animal.notifyPositionChanged(oldPosition, newPosition);

                    }
                    break;
            }
        }
    }

    @Override
    public String toString() {
        MapVisualizer visual = new MapVisualizer(this);
        return visual.draw(new Vector2d(0, 0), new Vector2d(this.width, this.height));
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        Animal animal = animalHashMap.get(oldPosition);
        animalHashMap.remove(oldPosition);
        animalHashMap.put(newPosition, animal);
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


    // tu powinno byc izi bo tu moga stac dwa na jednym
    @Override
    public boolean place(Animal animal) {
        if(!isOccupied(animal.getPosition())) {

            animals.add(animal);

            // added during lab6
            animalHashMap.put(animal.getPosition(), animal);
            return true;
        }
        else{
//            System.out.println("XD1");
            Object object = objectAt(animal.getPosition());
            // if grass takes the place, put the animal anyway
            if(object instanceof Grass){

                animals.add(animal);

                // added during lab6
                animalHashMap.put(animal.getPosition(), animal);
                return true; // important, because we succeed in placing animal
            }
            else{
                // added during lab 6
                throw new IllegalArgumentException("x:" + animal.getPosition().x + " " + "y: " + animal.getPosition().y + " jest juz zajęte");
//                return false;
            }
        }
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return animalHashMap.get(position) != null || grassHashMap.get(position) != null;
    }


    @Override
    public Object objectAt(Vector2d position) {
        Animal animal = animalHashMap.get(position);
        if(animal != null){
            return animal;
        }
        else{
            return grassHashMap.get(position);
        }
    }

    // HashMap Of Arrays Service
    public static void putToHashMap(LinkedHashMap hashMap, Vector2d position, Animal animal){

        ArrayList<Animal> list = (ArrayList<Animal>) hashMap.get(position);
        if(list == null){
            list = new ArrayList<Animal>();
            hashMap.put(position, list);
        }
        list.add(animal);
    }

    public static void removeFromHashMap(LinkedHashMap hashMap, String position, Animal animal){
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