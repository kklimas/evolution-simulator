package com.app.models;

abstract class AbstractMapEntity implements IMapEntity{
    protected Vector2d position;

    @Override
    public Vector2d getPosition() {
        return position;
    }
    @Override
    public void setPosition(Vector2d position) {
        this.position = position;
    }
}
