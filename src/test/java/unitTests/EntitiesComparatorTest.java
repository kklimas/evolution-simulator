package unitTests;

import com.app.configurations.CustomConfiguration;
import com.app.models.*;
import com.app.utils.EntitiesComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;

import static com.app.services.ConfigurationReaderService.getDefaultConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EntitiesComparatorTest {

    private WorldMap worldMap;
    private TreeSet<IMapEntity> entities;
    private final Vector2d position = new Vector2d(0, 0);
    private final CustomConfiguration configuration = getDefaultConfiguration();

    @BeforeEach
    void setup () {
        entities = new TreeSet<>(new EntitiesComparator());
    }

    @Test
    void compare1() {
        // given 3 animals at same position with different energy

        var a1 = new Animal(configuration, worldMap);
        var a2 = new Animal(configuration, worldMap);
        var a3 = new Animal(configuration, worldMap);

        a1.setPosition(position);
        a2.setPosition(position);
        a3.setPosition(position);

        a1.reduceEnergy(10);
        a2.reduceEnergy(5);
        a3.reduceEnergy(15);

        entities.addAll(List.of(a1, a2, a3));

        assertEquals(a2, entities.first());
    }

    @Test
    void compare2() {
        // given 2 animals and one plant at same position with different energy

        var a1 = new Animal(configuration, worldMap);
        var a2 = new Plant(configuration);
        var a3 = new Animal(configuration, worldMap);

        a1.setPosition(position);
        a2.setPosition(position);
        a3.setPosition(position);

        a1.reduceEnergy(10);
        a3.reduceEnergy(15);

        entities.addAll(List.of(a1, a2, a3));

        assertEquals(a1, entities.first());
        assertEquals(a2, entities.last());
    }

    @Test
    void compare3() {
        // given one animal and 2 plants at same position

        var a1 = new Animal(configuration, worldMap);
        var a2 = new Plant(configuration);
        var a3 = new Plant(configuration);

        a1.setPosition(position);
        a2.setPosition(position);
        a3.setPosition(position);

        a1.reduceEnergy(configuration.startEnergy());

        entities.addAll(List.of(a1, a2, a3));

        assertEquals(a1, entities.first());
        assertEquals(Plant.class, entities.last().getClass());
    }
}
