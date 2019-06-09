package Day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

public class FrequencyBody {
    private static final String FILE = "./src/Day1/frequencies";

    public int readFrequences(){
        int result = 0;
        String line;
        try
                (FileReader fileReader = new FileReader(new File(FILE));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line=bufferedReader.readLine())!=null){
             result += Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return result;
    }

    public int repeatedFrequency() {
        int result = 0;
        TreeSet<Integer> subSums = new TreeSet<>();
        subSums.add(0);
        String line;
        boolean duplicateFrequency = false;
        while (!duplicateFrequency) {

            try
                    (FileReader fileReader = new FileReader(new File(FILE));
                     BufferedReader bufferedReader = new BufferedReader(fileReader)) {

                while (((line = bufferedReader.readLine()) != null)) {
                    result += Integer.parseInt(line);
                    if (duplicateFrequency = subSums.contains(result)) break;
                    subSums.add(result);
                }
            } catch (IOException e) {
                e.getMessage();
            }
        }
        if (!duplicateFrequency) System.out.print("Brak powtórzeń, ostatni wynik to: ");
        return result;
    }
}
