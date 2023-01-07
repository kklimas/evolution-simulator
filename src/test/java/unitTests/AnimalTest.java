package unitTests;

import com.app.configurations.CustomConfiguration;
import com.app.entities.Animal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.app.utils.ConfigurationReaderService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimalTest {

    CustomConfiguration configuration = getDefaultConfiguration();

    @Test
    void reproductionTest() {
        var a1 = new Animal(configuration);
        var a2 = new Animal(configuration);

        a1.setCurrentEnergy(40);
        a2.setCurrentEnergy(60);

        var child = a1.getChild(a2);
        assertEquals(a1.getGenotype().size(), child.getGenotype().size());
        assertEquals(List.of(), List.of());
    }
}
