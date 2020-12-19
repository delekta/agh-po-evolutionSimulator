package Interfaces;

import Objects.Animal;
import Objects.Vector2d;

public interface IPositionChangeObserver {
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
}
