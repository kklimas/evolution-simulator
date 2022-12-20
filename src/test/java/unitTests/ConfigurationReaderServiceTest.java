package unitTests;

import com.app.enums.variants.MapVariant;
import com.app.enums.variants.MutationVariant;
import com.app.enums.variants.PlantGrowVariant;
import com.app.exceptions.InvalidConfigurationFileException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.app.services.ConfigurationReaderService.*;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationReaderServiceTest {

    private final String PATH = "src/test/resources/testConfiguration";

    @Test
    void test1() {
        // should read properties from valid file
        var initialValue = 10;
        var file1 = new File("%s/%s".formatted(PATH, "test1"));

        assertDoesNotThrow(() -> {
            var result = getConfigurationFromFile(file1);
            assertEquals(initialValue, result.mapWidth());
            assertEquals(initialValue, result.mapHeight());
            assertEquals(initialValue, result.animalsStartNumber());

            assertEquals(MapVariant.GLOBE, result.mapVariant());
            assertEquals(MutationVariant.HUMAN_FACTOR, result.mutationVariant());
            assertEquals(PlantGrowVariant.NOT_TOXIC, result.plantGrowVariant());
        });
    }

    @Test
    void test2() {
        // should throw error because of invalid properties
        var file2 = new File("%s/%s".formatted(PATH, "test2"));
        assertThrows(InvalidConfigurationFileException.class, () -> getConfigurationFromFile(file2));
    }

    @Test
    void test3() {
        // should return custom configuration
        var result = getDefaultConfiguration();
        assertEquals(50, result.mapWidth());
        assertEquals(30, result.mapHeight());
        assertEquals(MapVariant.GLOBE, result.mapVariant());
    }
}
