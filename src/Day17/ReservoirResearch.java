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

    void importData() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> coordinates = importFromFile.getData(FILE);
        coordinates.sort(String::compareTo);
        emptyRow = String.valueOf(SAND_SYMBOL).repeat(1000);
        createMap(coordinates);
    }

    private void createMap(ArrayList<String> coordinates) {
        map = new ArrayList<>();
        for (String line : coordinates) {
            ArrayList<Integer> xs = getCoordinates('x', line);
            ArrayList<Integer> ys = getCoordinates('y', line);
            int rows = ys.get(ys.size()-1);
            while (map.size()<rows+1){
                StringBuilder row = new StringBuilder(emptyRow);
                map.add(row);
            }

            for (Integer x : xs) {
                for (Integer y : ys) {
                    map.get(y).replace(x,x+1, String.valueOf(CLAY_SYMBOL));
                }
            }
        }
    }

    private ArrayList<Integer> getCoordinates(char variable, String line) {
        ArrayList<Integer> values = new ArrayList<>();
        int startIndex = line.indexOf(variable)+2;
        char endingChar = startIndex>4 ? '.' : ',';
        values.add(Integer.parseInt(line.substring(startIndex,line.indexOf(endingChar))));
        if (endingChar=='.'){
            int endingNumber = Integer.parseInt(line.substring(line.lastIndexOf('.')+1));
            int currentNumber = values.get(0)+1;
            while (currentNumber<=endingNumber){
                values.add(currentNumber++);
            }
        }
        return values;
    }

    @Override
    public void findSolution() {
        importData();
        Drop drop = new Drop();
        fall(drop);
    }

    private void fall(Drop drop) {
        switch (map.get(drop.y+1).charAt(drop.x)) {
            case SAND_SYMBOL:
                drop.fallsDawn();
                break;
            case WATER_SYMBOL:
            case CLAY_SYMBOL:
                map.get(drop.y+1).replace(drop.x,drop.x+1, String.valueOf(FLOW_SYMBOL));
                break;
            case FLOW_SYMBOL:
                int y = drop.y+1;
                int x = drop.x;
                while (map.get(drop.y+1).charAt(drop.x) == (CLAY_SYMBOL | WATER_SYMBOL) &&
                        map.get(drop.y).charAt(drop.x-1) == SAND_SYMBOL){
                    x--;
                    map.get(drop.y).replace(drop.x,drop.x+1, String.valueOf(FLOW_SYMBOL));
                }
                x = drop.x;
                while (map.get(drop.y+1).charAt(drop.x) == (CLAY_SYMBOL | WATER_SYMBOL) &&
                        map.get(drop.y).charAt(drop.x+1) == SAND_SYMBOL){
                    x++;
                    map.get(drop.y).replace(drop.x,drop.x+1, String.valueOf(FLOW_SYMBOL));
                }

        }
    }

    private class Drop {
        int x;
        int y;
        char menisk;

        public Drop() {
            this.x = 500;
            this.y = 0;
            this.menisk = FLOW_SYMBOL;
        }

        public void fallsDawn() {
            this.y++;
        }
    }
}
