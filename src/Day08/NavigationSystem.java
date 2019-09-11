package Day08;

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
        int valueOfMainRoot = calculateRootMetadata();
        System.out.println("Sum of metadata entries is " + metadataEntries);
        System.out.println("Sum of indicated (by metadata) metadates is " + valueOfMainRoot);
    }

    private void importData() {             // wypełnia tablicę DANYCH, integarami występującymi w pliku źródłowym
        ImportFromFile importFromFile = new ImportFromFile();
        String dataInputAsString = importFromFile.getData(FILE).get(0);
        for (String number : dataInputAsString.split(" ")) {
            inputData.add(Integer.parseInt(number));
        }
    }


    private int calculateRootMetadata() {
        int metadata = 0;                       // #zad2 - wartość metadanych tego korzenia
        int howManyChildren = nextInt();        // odczytaj kolejną liczbę i zapisz jako liczba dzieci
        int[] rootValuesOfChildes = new int[howManyChildren]; // #zad2 - tablica z wartościami metadanych dzieci
        int howManyData = nextInt();            // odczytaj kolejną liczbę i zapisz jako liczba danych
        for (int i=0;i<howManyChildren;i++) {       // iterując wszystkie dzieci
            rootValuesOfChildes[i]=                 // #zad2 - zapisz w odpowiednim miejscu w tablicy
                    calculateRootMetadata();        // obliczoną wartość metadanych danego dziecka
        }                                           // #zad1 - zwiększona też została wartość metadataEntrance

        while (howManyData > 0) {       //dopóki są jakieś metadane
            int value = nextInt();      //odczytaj metadaną i zapisz w zmiennej "Value"
            metadataEntries += value;   // #zad1 - zwiększ wartość metadataEntrance o "value"
            if (howManyChildren==0) {   // #zad2 - jeśli dany korzeń nie ma dzieci
                metadata+=value;        // #zad2 - to wartością jego metadanych jest wartość VALUE
            } else if (value<=howManyChildren) {        // #zad2 - jeśli dany korzeń nie ma dzieci i VALUE to liczba wskazująca na dowolne z dzieci
                metadata+=rootValuesOfChildes[value-1]; // #zad2 - to wartość jego metadanych zwiększ o wartość metadanych wskazanego dziecka
            }
            howManyData--;              //zmniejsz liczbę pozostałych dzieci do sprawdzenia
        }
        return metadata;        // #zad2 - zwróć wartość metadanych bierzącego korzenia
    }

    private int nextInt() {
        int integer = inputData.get(0);     //jako następną liczbę przypisz aktualnie pierwszą liczbę w ciągu danych
        inputData.remove(0);          //usuń pierwszą liczbę z ciągu danych
        return integer;
    }
}