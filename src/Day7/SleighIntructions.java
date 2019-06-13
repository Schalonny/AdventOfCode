package Day7;

import ImportData.ImportFromFile;

import java.util.ArrayList;
import java.util.Arrays;

public class SleighIntructions {
    private static final String FILE = "./src/Day7/instructions";
    private static final int NUMBER_OF_RULES = 101;
    private StringBuilder instructionsOrder = new StringBuilder();
    private char[][] previousAndNext = new char[NUMBER_OF_RULES][2];


    private void convertData() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        data.sort(String::compareTo);
        for (String datum : data) {
            System.out.println(datum);
            previousAndNext[data.indexOf(datum)][0] = datum.charAt(5);
            previousAndNext[data.indexOf(datum)][1] = datum.charAt(36);
            //wyciągamy i łączymy w pary dane poprzednik, następnik
        }
    }

    private void setInitialOrder() {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            instructionsOrder.append(ch);
        }
    }

    public void findSolution() {
        int pnIndex = 0;
        convertData();
        setInitialOrder();
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (previousAndNext[pnIndex][0] == ch) {
                int startingLocation = instructionsOrder.indexOf(String.valueOf(ch));
                int inputLocation = instructionsOrder.length() - 1;
                int positionOfSecondLetter = 0;
                while (previousAndNext[pnIndex][0] == ch && pnIndex < previousAndNext.length - 1) {
                    int tempPosition = instructionsOrder.indexOf(String.valueOf(previousAndNext[pnIndex][1]));
                    if (inputLocation > tempPosition) {
                        inputLocation = tempPosition;
                        positionOfSecondLetter = tempPosition;
                    }
                    inputLocation = Math.min(
                            inputLocation, instructionsOrder.indexOf(String.valueOf(previousAndNext[pnIndex][1])));
                    pnIndex++;
                }
                int trim = 0;
                if (startingLocation > positionOfSecondLetter) {
                    trim = positionOfSecondLetter + 1;
                }
                String stringToMove = instructionsOrder.substring(trim, startingLocation + 1);
                instructionsOrder.delete(trim, startingLocation + 1);
                inputLocation = Math.max(inputLocation - stringToMove.length(), 0);
                instructionsOrder.insert(inputLocation, stringToMove);
                System.out.println(ch + " " + instructionsOrder.toString());
            }
        }
    }

}
