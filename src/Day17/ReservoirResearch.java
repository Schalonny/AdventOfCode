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
    private int firstRow = 1024;
    private ArrayList<Drop> drops;

    private void importData() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> coordinates = importFromFile.getData(FILE);
        coordinates.sort(String::compareTo);
        emptyRow = String.valueOf(SAND_SYMBOL).repeat(1000);
        createMap(coordinates);
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
        fillSandWithFlowSymbol(drops.get(0));
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
        int waterThatStays = 0;
        for (StringBuilder line : map) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == WATER_SYMBOL) {
                    water++;
                    waterThatStays++;
                }
                if (line.charAt(i) == FLOW_SYMBOL){
                    water++;
                }
            }
        }
        System.out.println("We can gather " + (water - firstRow) + " thousand liters of floating water");
        System.out.println("We can gather " + (waterThatStays) + " thousand liters of water after source is dry out");
    }

    private char whatsBellow(int x, int y) {
        return map.get(y + 1).charAt(x);
    }

    private char whatsAt(int x, int y) {
        return map.get(y).charAt(x);
    }

    private void fall(char whatsBellow, Drop drop) {
        switch (whatsBellow) {
            case SAND_SYMBOL:
                drop.fallsDawn();
                fillSandWithFlowSymbol(drop);
                break;
            case FLOW_SYMBOL:
                break;
            case WATER_SYMBOL:
            case CLAY_SYMBOL:
                ArrayList<java.io.Serializable> scan = scan(drop);
                char charToFill = (boolean) scan.get(0) & (boolean) scan.get(2) ? WATER_SYMBOL : FLOW_SYMBOL;
                map.get(drop.y).replace((int) scan.get(1), (int) scan.get(3) + 1, String.valueOf(charToFill).repeat((int) scan.get(3) - (int) scan.get(1) + 1));
                if ((boolean) scan.get(2) & (boolean) scan.get(0)) {
                    drop.goesUp();
                } else {
                    if ((boolean) scan.get(0) & !((boolean) scan.get(2))) {
                        drop.x = (int) scan.get(3);
                    } else {
                        drop.x = (int) scan.get(1);
                        if (!((boolean) scan.get(2))) {
                            drops.add(new Drop((int) scan.get(3), drop.y));
                        }
                    }
                }
                break;
        }
    }

    private ArrayList<java.io.Serializable> scan(Drop drop) {
        ArrayList<java.io.Serializable> result = new ArrayList<>();
        int currentX = drop.x;
        while ((whatsBellow(currentX, drop.y) == CLAY_SYMBOL || whatsBellow(currentX, drop.y) == WATER_SYMBOL) &
                whatsAt(currentX, drop.y) != CLAY_SYMBOL) {
            currentX--;
        }
        result.add(whatsAt(currentX, drop.y) == CLAY_SYMBOL);
        currentX += (boolean) result.get(0) ? 1 : 0;
        result.add(currentX);
        currentX = drop.x;
        while ((whatsBellow(currentX, drop.y) == CLAY_SYMBOL || whatsBellow(currentX, drop.y) == WATER_SYMBOL) &
                whatsAt(currentX, drop.y) != CLAY_SYMBOL) {
            currentX++;
        }
        result.add(whatsAt(currentX, drop.y) == CLAY_SYMBOL);
        currentX += (boolean) result.get(2) ? -1 : 0;
        result.add(currentX);
        return result;
    }


    private void fillSandWithFlowSymbol(Drop drop) {
        map.get(drop.y).replace(drop.x, drop.x + 1, String.valueOf(ReservoirResearch.FLOW_SYMBOL));
    }

    static private class Drop {
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
