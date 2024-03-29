package ImportData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportFromFile {

    public ArrayList<String> getData(String fileName) {
        ArrayList<String> data = new ArrayList<>();
        String line;
        try
                (FileReader fileReader = new FileReader(new File(fileName));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                data.add(line);
            }
        } catch (
                IOException e) {
            e.getMessage();
        }
        return data;
    }

    public ArrayList<StringBuilder> getDataAsStringBuilders(String fileName) {
        ArrayList<StringBuilder> data = new ArrayList<>();
        String compareLine;
        try
                (FileReader fileReader = new FileReader(new File(fileName));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((compareLine = bufferedReader.readLine()) != null)) {
                data.add(new StringBuilder().append(compareLine));
            }
        } catch (
                IOException e) {
            e.getMessage();
        }
        return data;
    }
}
