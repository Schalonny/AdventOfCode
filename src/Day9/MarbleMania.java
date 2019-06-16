package Day9;

import ImportData.ImportFromFile;
import Intarface.Riddle;

public class MarbleMania implements Riddle {
    private static final String FILE = "./src/Day9/gameStatus";
    private static final int SCORE_MARBLE = 23;
    private int numberOfMarbles;
    private int numberOfPlayers = setInitialValues();
    private long[] playersScore = new long[numberOfPlayers];

    @Override
    public void findSolution() {
        play();
        System.out.println("The highest result for first game is " + findHighScore());
        numberOfMarbles = numberOfMarbles*100;
        playersScore = new long[numberOfPlayers];
        play();
        System.out.println("The highest result for second game (100 x more Marbles) is " + findHighScore());
    }

    private long findHighScore() {
        long maxScore = 0;
        for (long score : playersScore) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    private int setInitialValues() {
        ImportFromFile importFromFile = new ImportFromFile();
        String status = importFromFile.getData(FILE).get(0);
        numberOfMarbles = Integer.parseInt(status.substring(status.indexOf("worth") + 6, status.indexOf(" point")));
        return Integer.parseInt(status.substring(0, status.indexOf(" players")));
    }

    private void play() {
        Marble currentMarble = new Marble(0);
        for (int marble = 1; marble < numberOfMarbles + 1; marble++) {
            if (marble % SCORE_MARBLE == 0) {
                currentMarble = removeMarbleSevenBackwardsAndScore(currentMarble, marble);
            } else {
                currentMarble = addMarbleOnSecondPosition(currentMarble, marble);
            }
        }

    }

    private Marble addMarbleOnSecondPosition(Marble currentMarble, int addingMarble) {
        return new Marble(currentMarble.next, addingMarble);
    }

    private Marble removeMarbleSevenBackwardsAndScore(Marble currentMarble, int scoringMarble) {
        Marble marbleToRemove = currentMarble.previous.previous.previous.previous.previous.previous.previous;
        score(scoringMarble + marbleToRemove.getValue(), currentMarble.getValue() % numberOfPlayers);
        currentMarble = marbleToRemove.next;
        marbleToRemove.removeMarble();
        return currentMarble;
    }

    private void score(int score, int playerID) {
        playersScore[playerID] += score;
    }


    private static final class Marble {
        private int value;
        Marble previous;
        Marble next;

        // konstruktor pierwszej kulki
        Marble(int value) {
            this.value = value;
            this.next = this;
            this.previous = this;
        }
        // konstruktor kulki do postawienia za aktualną kulką
         Marble(Marble actualMarble, int value) {
            this.value = value;             //wartość
            this.next = actualMarble.next;      //jako następny ustaw to co przed chwilą poprzednik miał następne
            this.next.previous = this;      //jako poprzednik następnego od tego ustaw ten element;
            actualMarble.next = this;           //poprzedniemu jako następny ustaw ten element
            this.previous = actualMarble;       //jako poprzednik wstawiwanego ustaw poprzedni element
        }

        void removeMarble() {
            this.previous.next = this.next;
            this.next.previous = this.previous;
        }

        int getValue() {
            return value;
        }
    }


}
