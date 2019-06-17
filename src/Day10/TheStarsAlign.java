package Day10;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class TheStarsAlign implements Riddle {
    private static final String FILE = "./src/Day10/initialPosition";
    private ArrayList<Star> stars;
    private int maxX, minX, maxY, minY, yWidth, xWidth;
    private static final int INFINITY = 99999999;
    private int timeToWait = 0;

    public TheStarsAlign() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> rowData = importFromFile.getData(FILE);
        ArrayList<Star> data = new ArrayList<>();
        for (String star : rowData) {
            int x = Integer.valueOf(star.substring(11, 16));
            if (star.charAt(10) == '-') {
                x = -x;
            }
            int y = Integer.valueOf(star.substring(19, 24));
            if (star.charAt(18) == '-') {
                y = -y;
            }
            int vX = Integer.valueOf(star.substring(37, 38));
            if (star.charAt(36) == '-') {
                vX = -vX;
            }
            int vY = Integer.valueOf(star.substring(41, 42));
            if (star.charAt(40) == '-') {
                vY = -vY;
            }
            data.add(new Star(x, y, vX, vY));
        }
        this.stars = data;
    }

    @Override
    public void findSolution() {
        // setAreaBorders();
        int lastWidth = INFINITY;
        xWidth = INFINITY - 1;
        while (xWidth < lastWidth) {
            if (xWidth < 70) {
                System.out.println(timeToWait);
                showShy();
            }
            lastWidth = xWidth;
            timesFlow();
        }
    }

    private void timesFlow() {
        updateStarsPositions();
        updateAreaWidth();
        timeToWait++;
    }

    private void updateStarsPositions() {
        resetBorderValues();
        for (Star star : stars) {
            star.xPosition += star.xVelocity;
            star.yPosition += star.yVelocity;
            minX = Math.min(minX, star.xPosition);
            maxX = Math.max(maxX, star.xPosition);
            minY = Math.min(minY, star.yPosition);
            maxY = Math.max(maxY, star.yPosition);
        }
    }

    private void resetBorderValues(){
        minX = 1000;
        maxX = -1000;
        minY = 1000;
        maxY = -1000;
    }

    private void updateAreaWidth() {
        yWidth = maxY - minY;
        xWidth = maxX - minX;
    }

    private void showShy() {
        ArrayList<StringBuilder> skyLine = new ArrayList<>();
        for (int y = 0; y < yWidth+1; y++) {
            skyLine.add(new StringBuilder());
            for (int x = 0; x < xWidth; x++) {
                skyLine.get(y).append('-');
            }
        }
        for (Star star : stars) {
            skyLine.get(star.yPosition-minY).replace(star.xPosition - minX,star.xPosition - minX+1, "*");
        }
        for (StringBuilder line : skyLine) {
            System.out.println(line.toString());
        }
    }

    private class Star {
        int xPosition, yPosition, xVelocity, yVelocity;

        Star(int xPosition, int yPosition, int xVelocity, int yVelocity) {
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            this.xVelocity = xVelocity;
            this.yVelocity = yVelocity;
        }

        @Override
        public String toString() {
            return "Star{" +
                    "xPosition=" + xPosition +
                    ", yPosition=" + yPosition +
                    ", xVelocity=" + xVelocity +
                    ", yVelocity=" + yVelocity +
                    '}';
        }
    }
}
