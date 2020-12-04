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
    public HashMap<Vector2d, ArrayList<Animal>> animalHashMap = new HashMap<Vector2d, ArrayList<Animal>>();

    public HashMap<Vector2d, Grass> grassHashMap = new HashMap<>();

    public Map(int height, int width, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio) {
        this.height = height;
        this.width = width;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;

        setJungle();

    }

    public int getStartEnergy() {
        return this.startEnergy;
    }

    private void setJungle() {
        this.jungleHeight = (int) (this.height * jungleRatio);
        this.jungleWidth = (int) (this.width * jungleRatio);


        int x = (int) ((this.width / 2) - (this.jungleWidth / 2));
        int y = (int) ((this.height / 2) - (this.jungleHeight / 2));
        this.jungleStartPoint = new Vector2d(x, y);
    }

    public void run(int turns) {

        while (turns > 0) {
            turns--;
            ArrayList<ArrayList<Animal>> animalsArray = returnArray(animalHashMap);

            ListIterator<ArrayList<Animal>> animalsArrayListIterator = animalsArray.listIterator();

            while (animalsArrayListIterator.hasNext()) {
                ArrayList<Animal> animalsAtPosition = animalsArrayListIterator.next();
                ListIterator<Animal> animalListIterator = animalsAtPosition.listIterator();
                while (animalListIterator.hasNext()) {
                    Animal animal = animalListIterator.next();

                    animal.changeEnergy(-this.moveEnergy);
                    if (animal.getEnergy() < 0) {
//                        removeFromHashMap(animalHashMap, animal.getPosition(), animal);
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


            ArrayList<ArrayList<Animal>> animalsUpdatedArray = returnArray(animalHashMap);

            animalsArrayListIterator = animalsUpdatedArray.listIterator();

            while (animalsArrayListIterator.hasNext()) {
                ArrayList<Animal> animalsAtPosition = animalsArrayListIterator.next();
                ArrayList<Animal> twoStrongest = Animal.getTwoStrongest(animalsAtPosition);
                // if animalsAtPosition is in animalsUpdatedArray it must have at least one animal
                Vector2d position = animalsAtPosition.get(0).getPosition();

                // eating
                if (grassHashMap.containsKey(position) && animalsAtPosition.size() > 0) {
                    Animal strongestAnimal = twoStrongest.get(0);
                    strongestAnimal.changeEnergy(grassHashMap.get(position).getPlantEnergy());
                    grassHashMap.remove(position);
                }
                // reproduction
                else if (animalsAtPosition.size() > 1) {
                    Animal newAnimal = Animal.reproduce(twoStrongest.get(0), twoStrongest.get(1));
                    // Parents can reproduce when their enegy is too low, then i return null
                    if (newAnimal != null) {
                        placeNewAnimal(twoStrongest.get(0).getPosition(), newAnimal);
                    }
                }
            }

            placeNewGrasses();
            System.out.println(this);
        }
    }

    public void run2(int turns) {
        int t = 0;
        while (turns > 0) {
            turns--;

            ArrayList<ArrayList<Animal>> animalsCopy = new ArrayList<>(animalHashMap.values());

            for (ArrayList<Animal> animaList : animalsCopy) {
                ArrayList<Animal> animalListCopy = new ArrayList<>(animaList);
                for (Animal animal : animalListCopy) {
                    animal.changeEnergy(-this.moveEnergy);
                    if (animal.getEnergy() < 0) {
                        // ok ten animal jest z kopii
                        removeFromHashMap(animalHashMap, animal.getPosition(), animal);
                    }else{
                        animal.changeOrientation();
                        //zmiane zawartosci w hashmapie
                        Vector2d oldPosition = animal.getPosition();
                        move(animal);
                        Vector2d newPosition = animal.getPosition();

                        // [] zastanow się czy dobrze rozumiesz Observer!!!
                        animal.notifyPositionChanged(oldPosition, newPosition);
                    }
                }
            }

            animalsCopy = new ArrayList<>(animalHashMap.values());
            // tez pracuje na kopii bo takto for sie wkurwia

            for (ArrayList<Animal> animalsAtPosition : animalsCopy) {
                ArrayList<Animal> twoStrongest = Animal.getTwoStrongest(animalsAtPosition);
                // if animalsAtPosition is in animalsUpdatedArray it must have at least one animal
                Vector2d position = animalsAtPosition.get(0).getPosition();

                // eating
                if (grassHashMap.containsKey(position) && animalsAtPosition.size() > 0) {
                    Animal strongestAnimal = twoStrongest.get(0);
                    strongestAnimal.changeEnergy(grassHashMap.get(position).getPlantEnergy());
                    grassHashMap.remove(position);
                }
                // reproduction
                else if (animalsAtPosition.size() > 1) {
                    Animal newAnimal = Animal.reproduce(twoStrongest.get(0), twoStrongest.get(1));
                    // Parents can reproduce when their enegry is too low, then i return null
                    if (newAnimal != null) {
                        placeNewAnimal(twoStrongest.get(0).getPosition(), newAnimal);
                    }
                }
            }

            placeNewGrasses();
            t++;
            System.out.println("Turn: " + t);
            System.out.println(this);
        }
    }

    public void move(Animal animal) {
        animal.setPosition(convertPositionToMap(animal.getPosition().add(animal.getOrientation().toUnitVector())));
    }

    private void placeNewAnimal(Vector2d parentPosition, Animal newAnimal) {
        if (!areAllAroundOccupied(parentPosition)) {
            do {
                newAnimal.setPosition(parentPosition);
                newAnimal.changeOrientation();
                move(newAnimal);
                System.out.println("ptak" + parentPosition.x + " " + parentPosition.y);
            } while (isOccupied(newAnimal.getPosition()));
        } else {
            newAnimal.setPosition(parentPosition);
            newAnimal.changeOrientation();
            move(newAnimal);
        }
        putToHashMap(animalHashMap, newAnimal.getPosition(), newAnimal);

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


    // converts position to map when we go outside it
    private Vector2d convertPositionToMap(Vector2d position) {
        int x = position.x < 0 ? this.width : position.x;
        int y = position.y < 0 ? this.height : position.y;
        x = x % (this.width + 1);
        y = y % (this.height + 1);
        return new Vector2d(x, y);
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


    private void placeNewGrasses() {
        Random rand = new Random();
        int x;
        int y;
        int i = 0;
        // place grass in jungle
        do {
            x = rand.nextInt(jungleWidth + 1);
            y = rand.nextInt(jungleHeight + 1);
            x += jungleStartPoint.x;
            y += jungleStartPoint.y;
            //niweluje problem zapetlania gdy jungla jest pelna, mozna zrobic funkcje sprawdzajaca czy jungla pelna!
            i++;
        } while (isOccupied(new Vector2d(x, y)) && i < 10);
        Grass jungleGrass = new Grass(plantEnergy);
        grassHashMap.put(new Vector2d(x, y), jungleGrass);

        // place grass in normal area
        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);

        } while (isOccupied(new Vector2d(x, y)) || isOnJungle(x, y));

        Grass normalGrass = new Grass(plantEnergy);
        grassHashMap.put(new Vector2d(x, y), normalGrass);

    }

    // [] check if e.g. jungleStartPoint.x + width is included
    private boolean isOnJungle(int x, int y) {
        return jungleStartPoint.x < x && x <= jungleStartPoint.x + width
                && jungleStartPoint.y < y && y <= jungleStartPoint.y + height;
    }

    @Override
    public boolean place(Animal animal) {
        if (!isOccupied(animal.getPosition())) {
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
        if (animalArrayList != null) {
            // [] Do zastanowienia czy np nie chcemy wypisywać tego z największą energią
            return animalArrayList.get(0);
        } else {
            // If there is no grass - returns null
            return grassHashMap.get(position);
        }
    }

    // HashMap Of Arrays - Service
    public static void putToHashMap(HashMap hashMap, Vector2d position, Animal animal) {

        ArrayList<Animal> list = (ArrayList<Animal>) hashMap.get(position);
        if (list == null) {
            list = new ArrayList<Animal>();
            hashMap.put(position, list);
        }
        list.add(animal);
    }

    public void removeFromHashMap(HashMap hashMap, Vector2d position, Animal animal) {
        ArrayList<Animal> list = animalHashMap.get(position);
        if (list != null) {
            list.remove(animal);
            animalHashMap.put(position, list);
            if (list.isEmpty()) {
                animalHashMap.remove(position);
            }
        }
    }

    public static void showHashMapFor(HashMap hashMap) {
        ArrayList<ArrayList<Animal>> arrayOfArrays = returnArray(hashMap);

        for (ArrayList list : arrayOfArrays) {
            if (!list.isEmpty()) {
                for (Object element : list) {
                    System.out.print(element);
                }
                System.out.println(" ");
            }

        }
    }

    public static ArrayList<ArrayList<Animal>> returnArray(HashMap hashMap) {
        return new ArrayList<>(hashMap.values());
    }
}