package Interfaces;

import Classes.Vector2d;

public interface IPositionChangeObserver {
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
