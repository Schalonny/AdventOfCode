package Day01;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.TreeSet;

public class FrequencyBody implements Riddle {
    private static final String FILE = "./src/Day1/frequencies";
    private ArrayList<String> frequencies = new ArrayList<>();

    public void findSolution(){
        readFrequencies();
        System.out.println("Final frequency is " + sumOFFrequencies());
        System.out.println("First duplicate frequency is " + repeatedFrequency());
    }

    private void readFrequencies(){
        ImportFromFile importFromFile = new ImportFromFile();
        frequencies = importFromFile.getData(FILE);
    }

    private int sumOFFrequencies(){
        int result = 0;
        for (String frequency : frequencies) {
            result += Integer.parseInt(frequency);
        }
        return result;
    }

    private int repeatedFrequency() {
        TreeSet<Integer> subSums = new TreeSet<>();
        int subSum = 0;
        int iterator = 0;

        while (!subSums.contains(subSum)){
            subSums.add(subSum);
            subSum+=Integer.parseInt(frequencies.get(iterator%frequencies.size()));
            iterator ++;
        }

        return subSum;
    }
}
