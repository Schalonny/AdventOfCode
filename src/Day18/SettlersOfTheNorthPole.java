package Day18;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class SettlersOfTheNorthPole implements Riddle {
    private static final String FILE = "./src/Day18/initialMap";
    private static final char TREE = '|';
    private static final char OPEN = '.';
    private static final char LUMBERYARD = '#';

    private ArrayList<ArrayList<StringBuilder>> maps;
    private int mapSize;
    private int lineSize;
    private int frequency;
    private int firstOccurrence;


    public SettlersOfTheNorthPole() {
        maps = new ArrayList<>();
    }

    private void importData() {
        ImportFromFile importFromFile = new ImportFromFile();
        maps.add(importFromFile.getDataAsStringBuilders(FILE));
        mapSize = maps.get(0).size();
        lineSize = maps.get(0).get(0).length();
    }


    @Override
    public void findSolution() {
        importData();
        int iteration = 0;
        boolean twoSameMaps = false;
        while (iteration < 1000000000 && !twoSameMaps) {
            maps.add(new ArrayList<>());
            for (int y = 0; y < mapSize; y++) {
                maps.get(iteration + 1).add(new StringBuilder());
                for (int x = 0; x < lineSize; x++) {
                    char charToCopy = maps.get(iteration).get(y).charAt(x);
                    switch (maps.get(iteration).get(y).charAt(x)) {
                        case OPEN:
                            charToCopy = (neighboursWith(TREE, y, x, iteration) >= 3) ? TREE : OPEN;
                            break;
                        case TREE:
                            charToCopy = (neighboursWith(LUMBERYARD, y, x, iteration) >= 3) ? LUMBERYARD : TREE;
                            break;
                        case LUMBERYARD:
                            charToCopy = (neighboursWith(LUMBERYARD, y, x, iteration) > 0 &&
                                    neighboursWith(TREE, y, x, iteration) > 0) ? LUMBERYARD : OPEN;
                            break;
                    }
                    maps.get(iteration + 1).get(y).append(charToCopy);
                }
            }
            iteration++;
            for (int i = 0; i < iteration; i++) {
                twoSameMaps = compareMaps(maps.get(iteration), maps.get(i));
                if (twoSameMaps) {
                    frequency = iteration - i;
                    firstOccurrence = i;
                    break;
                }
            }
        }
    printResult();

    }

    private boolean compareMaps(ArrayList<StringBuilder> map1, ArrayList<StringBuilder> map2) {
        int numberOfSimilarities = 0;
        for (int i = 0; i < mapSize; i++) {
            if (map1.get(i).toString().equals(map2.get(i).toString())){
                numberOfSimilarities++;
            } else break;
        }
        return numberOfSimilarities == mapSize;
    }


    private void printResult() {
        int trees = 0;
        int lumberyards = 0;
        int mapNumber = (1000000000 - firstOccurrence) % frequency + firstOccurrence;
        for (StringBuilder line : maps.get(mapNumber)) {
            System.out.println(line);
            for (int i = 0; i < lineSize; i++) {
                if (line.charAt(i) == TREE) {
                    trees++;
                } else if (line.charAt(i) == LUMBERYARD) {
                    lumberyards++;
                }
            }
        }
        System.out.println(trees * lumberyards);
    }

    private int neighboursWith(char what, int y, int x, int iteration) {
        int result = 0;
        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < 2; i++) {
                if (i * i + j * j > 0 &&
                        y + i < mapSize &&
                        y + i >= 0 &&
                        x + j < lineSize &&
                        x + j >= 0 &&
                        (maps.get(iteration).get(y + i).charAt(x + j) == what)) {
                    result++;
                }
            }
        }
        return result;
    }
}
