package Interfaces;

import Classes.Animal;
import Classes.Vector2d;

public interface IPositionChangeObserver {
    // Animal argument added because, in animalHashMap in one position we could have more than one position
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
}
