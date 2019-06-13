package Day7;

import ImportData.ImportFromFile;

import java.util.*;

public class SleighIntructions {
    private static final String FILE = "./src/Day7/instructions";
    private StringBuilder finalOrder = new StringBuilder();
    private ArrayList<String> previousAndNext = new ArrayList<>();
    private TreeSet<Character> setOfInstructions = new TreeSet<>();


    private void convertData() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        data.sort(String::compareTo);
        for (String datum : data) {
            previousAndNext.add(datum.charAt(5) + "" + datum.charAt(36));
            //wyciągamy i łączymy w pary dane poprzednik, następnik
        }
    }

    private void setInitialOrder() {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            setOfInstructions.add(ch);
        }
    }

    private char whatCanBeFirst(TreeSet<Character> availableCharacters){
                                            // dostarczamy dostępne jeszcze znaki
        TreeSet<Character> charsWithoutPrevious = new TreeSet<>(availableCharacters);
                                            // kopiujemy listę dostępnych znaków, by nie modyfikować bazowej
                                            // ma przechowywać listę znaków, które wg instrukcji nie posiadają
                                            // wymuszonego poprzednika (nie ma takiej bezpośredniej komendy)
        for (String s : previousAndNext) {
            charsWithoutPrevious.remove(s.charAt(1));
        }                                   // przeglądamy wszystkie komendy i ze zboiru znaków usuwamy te, które
                                            // pojawiają się jako następnik w danej komendzie (bo jeśli coś
                                            // występuje jako następnik, to nie może być pierwszą literą)
        char firstChar = charsWithoutPrevious.first();
                                            // jako pierwszy znak przyjmuję ten bez poprzednikóœ, który alfabetycznie
                                            // jest na początku (zgodnie z treścią zadania)
        setOfInstructions.remove(firstChar);
                                            // z dostępnych jeszcze znaków wywalamy znaleziony znak
        previousAndNext.removeIf((s) -> s.charAt(0)==firstChar);
                                            // z listy komend usuwamy wszystkie te, które jako poprzednik mają wymieniony
                                            // usunięty właśnie znak, gdyż te komendy w sposób trywialny są spełnione
        return firstChar;
    }


    public void findSolution() {
        convertData();      // mam arrayListę z dwuliterowymi "słowami" poprzednik/następnik
        setInitialOrder();  // oraz zbiór wszystkich literek, które mamy dostępne startowo (A-Z)

        while (!setOfInstructions.isEmpty()){
            finalOrder.append(whatCanBeFirst(setOfInstructions));
        }                   // powtórz pętle znajdywania pierwszego dopóki jest conajmniej jeden znak

        System.out.println(finalOrder);
                            // wydrukuj rozwiązanie
    }

}
