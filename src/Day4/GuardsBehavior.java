package Day4;

import ImportData.ImportFromFile;

import java.util.*;

public class GuardsBehavior {
    private static final String INPUT = "./src/Day4/wallData";

    private ArrayList<String> getSortedData() {
        ImportFromFile dataImport = new ImportFromFile();
        ArrayList<String> guardsList = dataImport.getData(INPUT);
        Collections.sort(guardsList);
        return guardsList;
    }

    private Guard findSleeper() {
        int sleeperID = -1;
        int totalAsleep = -1;
        Map<Integer, Guard> mapOfGuards = createGuardsMap(getSortedData());
        for (Guard guard : mapOfGuards.values()) {
            if (guard.getTotalAsleep() > totalAsleep) {
                sleeperID = guard.getNumber();
                totalAsleep = guard.getTotalAsleep();
            }
        }
        return mapOfGuards.get(sleeperID);
    }

    // sleeper ID x minute his sleept most
    public int findControlSum() {
        Guard sleeper = findSleeper();
        int theBestMinute = 0;
        for (int i = 0; i < sleeper.getSleep().length; i++) {
            if (sleeper.getSleep()[i] > sleeper.getSleep()[theBestMinute]) {
                theBestMinute = i;
            }
        }
        return sleeper.getNumber() * theBestMinute;
    }

    //ta metoda ma pozwolić stworzyć mapę Strażników wyciągając ich z listy danych
    private Map<Integer, Guard> createGuardsMap(ArrayList<String> notes) {
        Map<Integer, Guard> timeTable = new HashMap<>();
        int guardNo = -1;
        // dopóki nikogo nie będzie na zmianiae to nie ma wartości numeru strażnika
        // lista danych jest posortowana, a więc musi zaczynać się od pójścia kogoś na zmianę
        for (String record : notes) {
            // czytaj kolejne rekordy
            // jeśli rekord zawiera rozpoczecie pracy
            if (record.contains("Guard #")) {
                //wtedy ustaw wskazany numer jako aktualny klucz
                guardNo = Integer.parseInt(
                        record.substring(
                                record.indexOf("Guard #") + 7, record.indexOf(" begins")));
                // jeśli mamy już w mapie gościa o tym numerze klucza
                if (timeTable.containsKey(guardNo)) {
                    // to zwiększ ilość jego zmian o 1
                    timeTable.get(guardNo).increaseShiftsByOne();
                    // natomiast jeśli nie było takiego gościa w rejestrze
                    // to po stwórz nowego (domyślne awake*60, liczba zmian 0)
                } else {
                    timeTable.put(guardNo, new Guard(guardNo));
                }
                // a jeśli przyszedł na zmianę spóźniony
                if (Integer.parseInt(record.substring(12, 14)) == 0) {
                    // to wszystkie minuty spóźnienia ustaw jako śpiące
                    for (int i = 0; i < Integer.parseInt(record.substring(15, 17)); i++) {
                        timeTable.get(guardNo).putToSleep(i);
                    }
                }
            } else {
                // jeśli rekord zawiera zaśnięcie to zmień statusy snu tego gościa od teraz do końca zmiany
                if (record.contains("falls asleep")) {
                    for (int i = Integer.parseInt(record.substring(15, 17)); i < 60; i++) {
                        timeTable.get(guardNo).putToSleep(i);
                    }
                } else
                    // jeśli rekord zawiera pobudkę to zmień statusy snu tego gościa od teraz do końca zmiany
                    if (record.contains("wakes up")) {
                        for (int i = Integer.parseInt(record.substring(15, 17)); i < 60; i++) {
                            timeTable.get(guardNo).awake(i);
                        }
                    }
            }
        }
        return timeTable;
    }

}
