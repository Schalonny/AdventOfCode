package Day9;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class MarbleMania implements Riddle {
    private static final String FILE = "./src/Day9/gameStatus";
    private static final int SCORE_MARBLE = 23;
    private static final int HOW_FAR_PUT_NEW = 2;
    private static final int WHERE_IS_BONUS_MARBLE = -7;
    private int numberOfMarbles;
    private int numberOfPlayers = setInitialValues();
    private ArrayList<Integer> marblesOrder = new ArrayList<>();
    private int[] playersScore = new int[numberOfPlayers];


    @Override
    public void findSolution() {
        play();
        System.out.println("The highest result for first game is " + findHighScore());
    }

    private void play(){
        marblesOrder.add(0);
        int currentPosition = 1;
        for (int marble = 1; marble < numberOfMarbles + 1; marble++) {
            if (marble % SCORE_MARBLE == 0) {
                currentPosition = positionOfMarkerAfterScore(currentPosition, marble);
            } else {
                currentPosition = positionOfMarkerAfterAddingMarble(currentPosition, marble);
            }
        }
    }

    private int positionOfMarkerAfterScore(int position, int marbleNumber){
        int idToTake = (marblesOrder.size() + position + WHERE_IS_BONUS_MARBLE) % marblesOrder.size();
        playersScore[marbleNumber % numberOfPlayers] += marbleNumber + marblesOrder.get(idToTake);
        marblesOrder.remove(idToTake);
        return idToTake;
    }

    private int positionOfMarkerAfterAddingMarble(int position, int marbleNumber){
        position = (position +HOW_FAR_PUT_NEW) % marblesOrder.size();
        marblesOrder.add(position, marbleNumber);
        return position;
    }

    private int findHighScore(){
        int maxScore = 0;
        for (int score : playersScore) {
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

}
