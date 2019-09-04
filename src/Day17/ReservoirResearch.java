package Day17;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class ReservoirResearch implements Riddle {
    private static final String FILE = "./src/Day17/clay";
    private static final char CLAY_SYMBOL = '#';
    private static final char SAND_SYMBOL = '.';
    private static final char FLOW_SYMBOL = '|';
    private static final char WATER_SYMBOL = '~';
    private ArrayList<StringBuilder> map;
    private String emptyRow;
    private int quantityOfWater;
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
        fillSandWith(FLOW_SYMBOL,drops.get(0));
        int dropsThatFinish = 0;
        while (drops.size()>dropsThatFinish) {
            Drop currentDrop = drops.get(dropsThatFinish);
            char whatsBellow = SAND_SYMBOL;
            while (currentDrop.y < map.size() & whatsBellow!=FLOW_SYMBOL) {
                whatsBellow= whatsBellow(currentDrop.x,currentDrop.y);
                fall(whatsBellow, currentDrop);
            }
            dropsThatFinish++;
        }
    }

    private char whatsBellow(int x, int y) {
        return map.get(y + 1).charAt(x);
    }

    private void fall(char whatsBellow, Drop drop) {
        switch (whatsBellow) {
            case SAND_SYMBOL:
                drop.fallsDawn();
                fillSandWith(WATER_SYMBOL, drop);
                break;
            case FLOW_SYMBOL:
                break;
            case WATER_SYMBOL:
            case CLAY_SYMBOL:
                int numberOfEndings = fillRowReturnNumberOfEndings(drop);
                if (numberOfEndings==2){
                    drop.goesUp();
                } else {
                    if (numberOfEndings==0){
                        drops.add(new Drop(maxX, drop.y));
                    }
                    drop.x = (whichEnd) ? minX : maxX; //might be right if "if" will be false
                }
                break;

        }
    }

    private int fillRowReturnNumberOfEndings(Drop drop) {
        int result=0;
        int currentX = drop.x;
        while (whatsBellow(currentX,drop.y)!=(CLAY_SYMBOL | WATER_SYMBOL)){
            currentX--;
            if (map.get(drop.y).charAt(currentX) == CLAY_SYMBOL){
                result++;
                break;
            }
        }
        int minX = currentX;
        currentX = drop.x;
        while (whatsBellow(currentX,drop.y)!=(CLAY_SYMBOL | WATER_SYMBOL)){
            currentX++;
            if (map.get(drop.y).charAt(currentX) == CLAY_SYMBOL){
                result++;
                break;
            }
        }
        int maxX = currentX;
        char charToFill = (result==2) ? WATER_SYMBOL : FLOW_SYMBOL;
        map.get(drop.y).replace(minX,maxX+1,String.valueOf(charToFill).repeat(maxX-minX+1));
        return result;
    }

    private void fillSandWith(char symbol, Drop drop) {
        map.get(drop.y).replace(drop.x, drop.x + 1, String.valueOf(symbol));
        quantityOfWater++;
    }

    private class Drop {
        int x;
        int y;

        public Drop(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Drop() {
            this.x = 500;
            this.y = 0;
        }

        public void fallsDawn() {
            this.y++;
        }

        public void goesUp() {
            this.y--;
        }
    }
}
