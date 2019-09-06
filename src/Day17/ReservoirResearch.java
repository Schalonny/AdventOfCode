package Day17;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class ReservoirResearch implements Riddle {
    private static final String FILE = "./src/Day17/clay";
    private static final char CLAY_SYMBOL = 'X';
    private static final char SAND_SYMBOL = '_';
    private static final char FLOW_SYMBOL = 'V';
    private static final char WATER_SYMBOL = '~';
    private ArrayList<StringBuilder> map;
    private String emptyRow;
    private int quantityOfWater;
    private int firstRow = 1024;
    ArrayList<Drop> drops;

    void importData() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> coordinates = importFromFile.getData(FILE);
        coordinates.sort(String::compareTo);
        emptyRow = String.valueOf(SAND_SYMBOL).repeat(1000);
        createMap(coordinates);
        quantityOfWater = 0;
        drops = new ArrayList<>();
    }

    private void createMap(ArrayList<String> coordinates) {
        map = new ArrayList<>();
        for (String line : coordinates) {
            ArrayList<Integer> xs = getCoordinates('x', line);
            ArrayList<Integer> ys = getCoordinates('y', line);
            int rows = ys.get(ys.size() - 1);
            while (map.size() < rows + 1) {
                StringBuilder row = new StringBuilder(emptyRow);
                map.add(row);
            }

            for (Integer x : xs) {
                for (Integer y : ys) {
                    map.get(y).replace(x, x + 1, String.valueOf(CLAY_SYMBOL));
                    firstRow = Math.min(firstRow, y);
                }
            }
        }
    }

    private ArrayList<Integer> getCoordinates(char variable, String line) {
        ArrayList<Integer> values = new ArrayList<>();
        int startIndex = line.indexOf(variable) + 2;
        char endingChar = startIndex > 4 ? '.' : ',';
        values.add(Integer.parseInt(line.substring(startIndex, line.indexOf(endingChar))));
        if (endingChar == '.') {
            int endingNumber = Integer.parseInt(line.substring(line.lastIndexOf('.') + 1));
            int currentNumber = values.get(0) + 1;
            while (currentNumber <= endingNumber) {
                values.add(currentNumber++);
            }
        }
        return values;
    }

    @Override
    public void findSolution() {
        importData();
        drops.add(new Drop());
        fillSandWith(FLOW_SYMBOL, drops.get(0));
        int dropsThatFinish = 0;
        while (drops.size() > dropsThatFinish) {
            Drop currentDrop = drops.get(dropsThatFinish);
            char whatsBellow = SAND_SYMBOL;
            while (currentDrop.y < (map.size() - 1) & whatsBellow != FLOW_SYMBOL) {
                whatsBellow = whatsBellow(currentDrop.x, currentDrop.y);
                fall(whatsBellow, currentDrop);
            }
            dropsThatFinish++;
        }
        int water = 0;
        for (StringBuilder line : map) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == WATER_SYMBOL || line.charAt(i) == FLOW_SYMBOL) {
                    water++;
                }
            }
        }
        System.out.println("We can gather " + (water - firstRow) + " thousand liters of water");
    }

    private char whatsBellow(int x, int y) {
        return map.get(y + 1).charAt(x);
    }

    private void fall(char whatsBellow, Drop drop) {
        switch (whatsBellow) {
            case SAND_SYMBOL:
                drop.fallsDawn();
                fillSandWith(FLOW_SYMBOL, drop);
                break;
            case FLOW_SYMBOL:
                break;
            case WATER_SYMBOL:
            case CLAY_SYMBOL:
                int numberOfEndings = 0;
                int freeEnd = 0;
                int currentX = drop.x;
                while (whatsBellow(currentX, drop.y) == CLAY_SYMBOL || whatsBellow(currentX, drop.y) == WATER_SYMBOL) {
                    currentX++;
                    if (map.get(drop.y).charAt(currentX) == CLAY_SYMBOL) {
                        numberOfEndings++;
                        break;
                    }
                }
                int maxX = currentX;
                currentX = drop.x;
                while (whatsBellow(currentX, drop.y) == CLAY_SYMBOL || whatsBellow(currentX, drop.y) == WATER_SYMBOL) {
                    currentX--;
                    if (map.get(drop.y).charAt(currentX) == CLAY_SYMBOL) {
                        numberOfEndings++;
                        freeEnd = maxX;
                        break;
                    } else freeEnd = currentX;
                }
                int minX = currentX;

                char charToFill = numberOfEndings == 2 ? WATER_SYMBOL : FLOW_SYMBOL;
                map.get(drop.y).replace(minX + 1, maxX, String.valueOf(charToFill).repeat(maxX - minX - 1));
                switch (numberOfEndings) {
                    case 0:
                        Drop newDrop = new Drop(maxX, drop.y - 1);
                        drops.add(newDrop);
                        drop.x = freeEnd;
                        break;
                    case 1:
                        drop.x = freeEnd;
                        fillSandWith(FLOW_SYMBOL, drop);
                        break;
                    case 2:
                        drop.goesUp();
                        break;
                }
                break;
        }
    }


    private void fillSandWith(char symbol, Drop drop) {
        map.get(drop.y).replace(drop.x, drop.x + 1, String.valueOf(symbol));
        quantityOfWater++;
    }

    private class Drop {
        int x;
        int y;

        Drop(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Drop() {
            this.x = 500;
            this.y = 0;
        }

        void fallsDawn() {
            this.y++;
        }

        void goesUp() {
            this.y--;
        }
    }
}
