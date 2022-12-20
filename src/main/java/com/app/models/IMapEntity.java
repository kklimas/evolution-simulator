package com.app.models;

import javafx.scene.shape.Shape;

public interface IMapEntity {
    void setPosition(Vector2d position);

    void selectPosition();

    Vector2d getPosition();

    Shape getShape();
}
