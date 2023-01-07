package unitTests;

import com.app.models.CSVRecord;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.app.utils.CSVWriterService.writeToCSV;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVWriterServiceTest {

    private final String DIR_PATH = "src/test/resources/csvTest/%s.csv";

    @Test
    void test1() {
        // given first record
        var csvRecord = new CSVRecord(
                1,
                5,
                5,
                5,
                5,
                5,
                5
        );
        writeToCSV(DIR_PATH.formatted("test1"), csvRecord);

        File file = new File(DIR_PATH.formatted("test1"));
        assertTrue(file.exists());

        // read

        assertTrue(file.delete());
    }

    @Test
    void test2() throws IOException {
        File file = new File(DIR_PATH.formatted("test2"));
        assertTrue(file.createNewFile());

        // given n-th record
        var csvRecord = new CSVRecord(
                44,
                5,
                5,
                5,
                5,
                5,
                5
        );

        // read file

        assertTrue(file.delete());
    }
}
