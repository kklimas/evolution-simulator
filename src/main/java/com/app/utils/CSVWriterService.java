package com.app.utils;

import com.app.models.CSVRecord;

import java.io.*;

public class CSVWriterService {

    public static void writeToCSV(String filePath, CSVRecord csvRecord) {
        File file = new File(filePath);
        BufferedWriter writer;
        try {
            if (!file.exists() && file.createNewFile()) {
                writer = new BufferedWriter(new FileWriter(filePath));
                var header = "DAY,ANIMALS_NUMBER,AVERAGE_ENERGY,ANIMAL_LIFE_EXPECTANCY,PLANTS_NUMBER,FREE_PLACES,DEAD_ANIMALS\n";
                writer.append(header);

            } else {
                writer = new BufferedWriter(new FileWriter(filePath, true));
            }
            writer.append(csvRecord.toString() + "\n");
            writer.close();
        } catch (IOException ignored) {}
    }
}
