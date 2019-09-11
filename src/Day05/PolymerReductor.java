package Day05;

import ImportData.ImportFromFile;
import Intarface.Riddle;

public class PolymerReductor implements Riddle {
    private static final String FILE = "./src/Day5/polymer";

    private StringBuilder getPolymer() {
        ImportFromFile importFromFile = new ImportFromFile();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(importFromFile.getData(FILE).get(0));
        return stringBuilder;
    }

    @Override
    public void findSolution() {

    }

    private StringBuilder reducePolymer(StringBuilder polymer) {
        int i = 0; //indeks na polimerze
        //dopóki nie osiągnęliśmy końca polimeru
        while (i < polymer.length() - 1) {
            //porównaj sąsiedni znak
            char a = polymer.charAt(i);
            char b = polymer.charAt(i + 1);
            if (a != b && Character.toUpperCase(a) == Character.toUpperCase(b)) {
                //jeśli były takie same co do wartości, a różne co do wielkości
                //usuń je i powtórz dla indeksu o jeden mniej
                polymer.deleteCharAt(i + 1);
                polymer.deleteCharAt(i);
                i = Math.max(i - 1, 0);
            } else i++;
            //jeśli były różne przesuń indeks jedno pole dalej
        }
        return polymer;
    }

    private int getPolymerLength(StringBuilder polymer) {
        return reducePolymer(polymer).length();
    }

    public int removeProblematicUnit() {
        // ściągnij oryginalny, już wykrojony polimer
        StringBuilder polymer = reducePolymer(getPolymer());
        // ustaw domyślnie, że to on jest najkrótszy
        int shortestPolymer = polymer.length();
        int polymerLength = shortestPolymer;
        // dodajmy strukturę próbnych polimerów od a do z (domyślnie - pusty)
        StringBuilder temporaryPolymer = new StringBuilder();
        // dla każdej litery w alfabecie
        for (char letter = 'a'; letter <= 'z'; letter++) {
            //do PUSTEGO próbnego polimera dodaj polimer wejściowy
            temporaryPolymer.append(polymer);
            //przejedź przez wszystkie pozycje
            for (int i = polymerLength-1; i >= 0; i--) {
                // jeśli znak na wskazanej pozycji co do wielkości jest równy sprawdzanej
                // literze z alfabetu to go usuń z próbnego polimera
                if (Character.toLowerCase(temporaryPolymer.charAt(i)) == letter) {
                    temporaryPolymer.deleteCharAt(i);
                }
            }
            // ustaw wartość najkrótszego polymera jako minimum z dotychczasowej wielkości i
            // wielkości zredukowanego próbnego polimera
            shortestPolymer = Math.min(shortestPolymer, getPolymerLength(temporaryPolymer));
            // wyczyść próbny polimer
            temporaryPolymer.delete(0,temporaryPolymer.length());
        }
        return shortestPolymer;
    }
}
