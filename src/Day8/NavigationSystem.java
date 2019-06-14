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

    private void importData() {             // wypełnia tablicę DANYCH, integarami występującymi w pliku źródłowym
        ImportFromFile importFromFile = new ImportFromFile();
        String dataInputAsString = importFromFile.getData(FILE).get(0);
        for (String number : dataInputAsString.split(" ")) {
            inputData.add(Integer.parseInt(number));
        }
    }


    private void calculateRootMetadata() {
        int howManyChildren = nextInt();    //odczytaj kolejną liczbę i zapisz jako liczba dzieci
        int howManyData = nextInt();        //odczytaj kolejną liczbę i zapisz jako liczba danych
        while (howManyChildren > 0) {       //dopóki są dzieci
            calculateRootMetadata();        //powtórz operację na dziecku
            howManyChildren--;              //zmniejsz liczbę dzieci
        }                                   //po przejechaniu wszystkich dzieci, następne dane to metadane
        while (howManyData > 0) {           //dopóki są jakieś metadane
            metadataEntries += nextInt();   //do metadanych dodaj kolejną liczbę
            howManyData--;                  //zmniejsz liczbę dzieci
        }
    }

    private int nextInt() {
        int integer = inputData.get(0);     //jako następną liczbę przypisz aktualnie pierwszą liczbę w ciągu danych
        inputData.remove(0);          //usuń pierwszą liczbę z ciągu danych
        return integer;
    }
}