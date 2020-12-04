package Classes;

public class World {
    public static void main(String[] args) {
        int height = 12;
        int width = 30;
        int startEnergy = 99;
        int moveEnergy = 1;
        int plantEnergy = 100;
        double jungleRatio = 0.1;
        Map map = new Map(height, width, startEnergy, moveEnergy, plantEnergy, jungleRatio);
        map.place(new Animal(map, new Vector2d(15, 5)));
        map.place(new Animal(map, new Vector2d(15, 6)));
        map.place(new Animal(map, new Vector2d(15, 7)));
        map.place(new Animal(map, new Vector2d(14, 5)));
        map.place(new Animal(map, new Vector2d(14, 6)));
        map.place(new Animal(map, new Vector2d(14, 7)));
        map.place(new Animal(map, new Vector2d(16, 5)));
        map.place(new Animal(map, new Vector2d(16, 6)));
        map.place(new Animal(map, new Vector2d(16, 7)));

        int turns = 500;
        System.out.println(map.toString());
        map.run2(turns);
        // co nie dziala
        // - zawijanie na druga strone [done]
        // - usun gdy energy mniejsze niz 0
        // problem bo placeGrasses sie zapetla
    }
}
