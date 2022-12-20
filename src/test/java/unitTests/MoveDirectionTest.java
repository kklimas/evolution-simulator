package unitTests;

import com.app.enums.MoveDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveDirectionTest {

    @Test
    void next1() {
        var currentDirection = MoveDirection.N;
        var rotation = 3;

        var nextDirection = currentDirection.next(rotation);
        assertEquals(MoveDirection.SE, nextDirection);
    }

    @Test
    void next2() {
        var currentDirection = MoveDirection.S;
        var rotation = 5;

        var nextDirection = currentDirection.next(rotation);
        assertEquals(MoveDirection.NE, nextDirection);
    }

    @Test
    void next3() {
        var currentDirection = MoveDirection.NW;
        var rotation = 8;

        var nextDirection = currentDirection.next(rotation);
        assertEquals(MoveDirection.NW, nextDirection);
    }
}
