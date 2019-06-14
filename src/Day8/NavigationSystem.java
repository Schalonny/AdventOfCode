package Day8;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class NavigationSystem implements Riddle {
    private static final String FILE = "./src/Day8/listOfNumbers";
    private ArrayList<Integer> inputData = new ArrayList<>();
    private int metadataEntries = 0;

    @Override
    public void findSolution() {
        importData();
        calculateRootMetadata();
        System.out.println(metadataEntries);
    }

    private void importData() {
        ImportFromFile importFromFile = new ImportFromFile();
        String dataInputAsString = importFromFile.getData(FILE).get(0);
        for (String number : dataInputAsString.split(" ")) {
            inputData.add(Integer.parseInt(number));
        }
    }


    private void calculateRootMetadata() {
        int howManyChildren = nextInt();
        int howManyData = nextInt();
        while (howManyChildren > 0) {
            calculateRootMetadata();
            howManyChildren--;
        }
        while (howManyData > 0) {
            metadataEntries += nextInt();
            howManyData--;
        }

    }

    private int nextInt() {
        int integer = inputData.get(0);
        inputData.remove(0);
        return integer;
    }
}
