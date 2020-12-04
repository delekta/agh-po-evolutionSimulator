package Classes;

public class World {
    public static void main(String[] args) {
        int height = 12;
        int width = 30;
        int startEnergy = 3;
        int moveEnergy = 1;
        int plantEnergy = 3;
        double jungleRatio = 0.1;
        Map map = new Map(height, width, startEnergy, moveEnergy, plantEnergy, jungleRatio);
        map.place(new Animal(map, new Vector2d(4, 5)));
        map.place(new Animal(map, new Vector2d(1, 2)));
        map.place(new Animal(map, new Vector2d(2, 2)));
        map.place(new Animal(map, new Vector2d(1, 3)));
        map.place(new Animal(map, new Vector2d(7, 3)));
        map.place(new Animal(map, new Vector2d(2, 3)));
        map.place(new Animal(map, new Vector2d(9, 3)));
        map.place(new Animal(map, new Vector2d(18, 12)));
        map.place(new Animal(map, new Vector2d(15, 9)));

        int turns = 5;
        map.run2(turns);
        // co nie dziala
        // - zawijanie na druga strone [done]
        // - usun gdy energy mniejsze niz 0
    }
}
