package Day7;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.*;

public class SleighInstructions implements Riddle {
    private static final String FILE = "./src/Day7/instructions";
    private static final int NUMBER_OF_HELPERS = 4;

    private StringBuilder finalOrder = new StringBuilder();
    private ArrayList<String> previousAndNext = new ArrayList<>();
    private TreeSet<Character> setOfTasks = new TreeSet<>();

    private int availableWorkers = NUMBER_OF_HELPERS + 1;
    private Worker[] workers = new Worker[availableWorkers];


    public void findSolution() {
        orderForOneWorker();
        timeForManyWorkers();
    }

    //################################# FIRST TASK #####################################333threethreethree

    private void orderForOneWorker() {
        convertData();      // mam arrayListę z dwuliterowymi "słowami" poprzednik/następnik
        setSetOfTasks();    // oraz zbiór wszystkich literek, które mamy dostępne startowo (A-Z)

        while (!setOfTasks.isEmpty()) {     // dopóki zbiór zadań nie jest pusty,
            finalOrder.append(whatMustBeFirst(setOfTasks));  // to na koniec listy zadań dodaj to, które musi być pierwsze
        }                                                    // z bierzącej listy zadań

        System.out.println("One men should work with order as shown: " + finalOrder);
        // wydrukuj rozwiązanie

    }

    private void convertData() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        data.sort(String::compareTo);
        for (String datum : data) {
            previousAndNext.add(datum.charAt(5) + "" + datum.charAt(36));
            //wyciągamy i łączymy w pary dane poprzednik, następnik
        }
    }

    private void setSetOfTasks() {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            setOfTasks.add(ch);
        }
    }

    private char whatMustBeFirst(TreeSet<Character> availableTasks) {
        TreeSet<Character> tasksWithoutPrevious = new TreeSet<>(availableTasks);
        // kopiujemy listę dostępnych znaków, by nie modyfikować bazowej
        // ma przechowywać listę znaków, które wg instrukcji nie posiadają
        // wymuszonego poprzednika (nie ma takiej bezpośredniej komendy)
        for (String s : previousAndNext) {
            tasksWithoutPrevious.remove(s.charAt(1));
        }                                   // przeglądamy wszystkie komendy i ze zboiru znaków usuwamy te, które
        // pojawiają się jako następnik w danej komendzie (bo jeśli coś
        // występuje jako następnik, to nie może być pierwszą literą)
        char firstTask = tasksWithoutPrevious.first();
        // jako pierwszy znak przyjmuję ten bez poprzednikóœ, który alfabetycznie
        // jest na początku (zgodnie z treścią zadania)
        setOfTasks.remove(firstTask);
        // z dostępnych jeszcze znaków wywalamy znaleziony znak
        previousAndNext.removeIf((s) -> s.charAt(0) == firstTask);
        // z listy komend usuwamy wszystkie te, które jako poprzednik mają wymieniony
        // usunięty właśnie znak, gdyż te komendy w sposób trywialny są spełnione
        return firstTask;
    }

//########################################### TASK 2 #############################################

    private void timeForManyWorkers() {
        convertData();      // mam arrayListę z dwuliterowymi "słowami" poprzednik/następnik
        setSetOfTasks();    // oraz zbiór wszystkich literek, które mamy dostępne startowo (A-Z)
        hireWorkers();      // uzupełnij tablicę pracowników pracownikami

        int usedTime = 0;
        while (isSomeoneWorking(setOfTasks)) {
            usedTime++;     // dopóki ktoś pracuje nad zestawem zadań, upłyń nieubłagana sekundo
        }

        System.out.println("To build sleights with " + NUMBER_OF_HELPERS + " elfes to help, you need " + usedTime + " second(s).");
    }

    private void hireWorkers() {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker();
        }                   // wypełniamy tablicę pracowników (5) pracownikami

    }

    private boolean isSomeoneWorking(TreeSet<Character> availableTasks) {   //liczba powtórzeń tej metody będzie rozwiązaniem
        for (Worker worker : workers) {             // pętla przejeżdża przez wszystkich pracowników
            worker.timesFlows();                    // dany pracownik zużywa czas
            if (worker.getTimeToFinish() == 0) {    // jeśli właśnie w tym momencie skończył pracować
                availableWorkers++;                 // zwiększ liczbę dostępnych pracowników
                previousAndNext.                    // z intrukcji usuwamy wszystkie zapiski o danym zadaniu jako poprzedniku
                        removeIf((s) -> s.charAt(0) == worker.getCurrentTask());
                worker.setCurrentTask('-');         // ustaw jego aktualne zadanie na "null"
            }
        }

        TreeSet<Character> tasksNextInOrder = new TreeSet<>(availableTasks); //tworzymy listę potencjalnych zadań

        for (String s : previousAndNext) {
            tasksNextInOrder.remove(s.charAt(1));   // usuwamy zadania, które wymagają podjęcia wcześniejszych kroków
        }                                           // w zmiennej tasksNextInOrder zostają tylko już dostępne zadania

        for (Worker worker : workers) {             // sprawdzamy jeszcze raz pracowników wszystkich
            if (worker.getTimeToFinish() <= 0 && !tasksNextInOrder.isEmpty()) { // jeżeli jest wolny i jest co robić
                char taskToAssign = tasksNextInOrder.first();   // ustal, które zadanie zostanie przypisane (pierwsze)
                assignTask(worker, taskToAssign);       // przypisz temu pracownikowi zadanie
                tasksNextInOrder.remove(taskToAssign);  // z dostępnych zadań usuń właśnie przypisane
            }
        }

        return (availableWorkers != NUMBER_OF_HELPERS + 1);
        // jeżeli po upłynięciu sekundy nie wszyscy są wolni, to znaczy, że prace wre
    }


    private void assignTask(Worker worker, char task) {
        worker.setCurrentTask(task);        // przypisz zadanie
        worker.setTimeToFinish(task - 4);   // ustal czas trwania zadania (60 sec + kolejnosć w alfabecie, np A=61, C=63)
        setOfTasks.remove(task);            // ze zbioru dostępnych zadań usuń właśnie przypisane
        availableWorkers--;                 // obniż ilość wolnych pracowników o 1
    }
}
