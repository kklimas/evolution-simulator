package unitTests;

import com.app.configurations.CustomConfiguration;
import com.app.entities.Animal;
import com.app.utils.Engine;
import org.junit.jupiter.api.Test;

import static com.app.utils.ConfigurationReaderService.*;
import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    CustomConfiguration configuration = getDefaultConfiguration();
    Engine engine;

    @Test
    void reproductionTest() {
        var a1 = new Animal(configuration, engine);
        var a2 = new Animal(configuration, engine);

        a1.setCurrentEnergy(40);
        a2.setCurrentEnergy(60);

        var child = a1.getChild(a2);
        assertEquals(a1.getGenotype().size(), child.getGenotype().size());
    }

    @Test
    void readinessForReproductionTest() {
        var a1 = new Animal(configuration, engine);
        var a2 = new Animal(configuration, engine);

        a1.setCurrentEnergy(40);
        a2.setCurrentEnergy(60);

        a1.reproduce();
        a2.reproduce();

        assertFalse(a1.isReadyForReproduction());
        assertFalse(a2.isReadyForReproduction());

        a1.move();
        a2.move();

        assertTrue(a1.isReadyForReproduction());
        assertTrue(a2.isReadyForReproduction());
    }
}
