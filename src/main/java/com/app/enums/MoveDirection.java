package com.app.enums;

import com.app.models.Vector2d;

public enum MoveDirection {
    N, NE, E, SE, S, SW, W, NW;

    public MoveDirection next(int rotation) {
        var current = this.ordinal();
        var directions = MoveDirection.values();
        var nextIndex = (current + rotation) % directions.length;
        return directions[nextIndex];
    }

    public Vector2d toVector2d() {
        return switch (this) {
            case N -> new Vector2d(0, -1);
            case NE -> new Vector2d(1, -1);
            case E -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, 1);
            case S -> new Vector2d(0, 1);
            case SW -> new Vector2d(-1, 1);
            case W -> new Vector2d(-1, 0);
            default -> new Vector2d(-1, -1);
        };
    }
}
