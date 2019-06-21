package Day12;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.HashMap;


public class SubterraneanSustainability implements Riddle {
    private static final String FILE = "./src/Day12/reproductionRules";
    private static final String INITIAL_STATE =
            "#....#.#....#....#######..##....###.##....##.#.#.##...##.##.#...#..###....#.#...##.###.##.###...#..#";
    private static final long FINAL_GENERATION = 50_000_000_000L;
    private HashMap<String, Character> rules;
    private int firstCharWithSeed;

    public SubterraneanSustainability() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        // w zmiennej "data" zapisz wszystkie wiersze zasad z pliku wejściowego
        this.rules = new HashMap<>();
        for (String datum : data) {
            // każdy wiersz zapisz jako obiekt w HashMapie, jako indentyfikator biorąc układ wyjściowey
            // jako wartość przyjmując wynik takiego układu
            this.rules.put(datum.substring(0, 5), datum.charAt(datum.length() - 1));
        }

    }


    @Override
    public void findSolution() {
        String oldGeneration = ".";
        // przyjmij poprzednie pokolenie jako puste
        String newGeneration = INITIAL_STATE;
        // nowe pokolenie (startowo stan zastany)
        int generation = 0;
        // liczba dotychczasowych pokoleń
        while (!oldGeneration.equals(newGeneration))
        {
            //dopóki kolejne pokolenia nie będą wyglądać identycznie (stan ustalony) z dokładnością do przesunięcia
            // generuj kolejne pokolenie
            generation++;
            oldGeneration = newGeneration;
            // "zestarzej" bierzące pokolenie
            newGeneration = addGeneration(oldGeneration);
            // wyznacz nowe pokolenie za pomocą metody addGeneration
            if (generation==20)
            {
                // break point dla zadania pierwszego - kontrolna suma po 20 pokoleniach
                System.out.println("After 20 generations plants will be seeded in way to generate control sum of "
                        + calculateControlSum(newGeneration, firstCharWithSeed));
            }
        }
            // po opuszczeniu pętli sprawdź jaki jest stosunek obecnego pokolenia do przesunięcia roślin
            int generationDelay = generation - firstCharWithSeed;
            // na podstawie wartości finalnego pokolenia wyznacz przesunięcie roślin i oblicz sumę kontrolną
            System.out.println("After 50.000.000.000 generations, seeds will be seeded in way that generates control sum of "
                    + calculateControlSum(newGeneration, FINAL_GENERATION - generationDelay));

    }

    private String addGeneration(String oldGeneration) {
        StringBuilder newGeneration = new StringBuilder();
        // dodaj nowe pokolenie jako pusty StringBuilder
        oldGeneration = "....." + oldGeneration + ".....";
        // do starego pokolenia dodaj na przód i koniec 5 pustych doniczek
        for (int i = 2; i < oldGeneration.length() - 3; i++) {
            // począwszy od 3 doniczki (z rozpatrywanych) ustal nową wartość #/. na podstawie zasad
            // zakończ na doniczce 3 od końca
            newGeneration.append(newValue(oldGeneration.substring(i - 2, i + 3)));
        }
        // ustaw flagę pierwszego Ziarna na aktualne pierwsze wystąpienie roślinki (-3, ponieważ
        // dodając 5 doniczek rozpatrywanych z przodu, daliśmy 3 pozycje z przodu w nowej generacji -
        // zaczynaliśmy od trzeciej, stąd jeśli pierwsze wystąpienie # było na 3 pozycji, to znaczy, że się nie przesunęło)
        firstCharWithSeed += newGeneration.indexOf("#") - 3;
        while (newGeneration.charAt(0) == '.') {
            newGeneration.deleteCharAt(0);
        }
        while (newGeneration.charAt(newGeneration.length() - 1) == '.') {
            newGeneration.deleteCharAt(newGeneration.length() - 1);
        }
        // usuwamy wszystkie "." niepotrzebne z przodu i z tyłu nowej generacji
        return newGeneration.toString();
    }

    private long calculateControlSum (String generation, long firstCharWithSeed){
        long controlSum = 0;
        for (int i = 0; i < generation.length(); i++) {
            // dla każdego znaku w napisie
            if (generation.charAt(i) == '#') {
                // jeśli jest to # (ziarno) dodaj jego pozycję do sumy kontrolnej (uwzględnij, że zerowy znak
                // napisu to tak naprawdę roślinka w doniczce numer firstCharWithSeed)
                controlSum += firstCharWithSeed + i;
            }
        }
        return controlSum;
    }



    private char newValue(String fiveChars) {
        return rules.get(fiveChars);
    }

}
