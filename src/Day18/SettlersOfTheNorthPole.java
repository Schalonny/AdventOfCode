package Day18;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class SettlersOfTheNorthPole implements Riddle {
    private static final String FILE = "./src/Day18/initialMap";
    private static final char TREE = '|';
    private static final char OPEN = '.';
    private static final char LUMBERYARD = '#';

    private ArrayList<StringBuilder> map;
    private ArrayList<ArrayList<StringBuilder>> maps;
    private int mapSize;
    private int lineSize;


    public SettlersOfTheNorthPole() {
        map = new ArrayList<>();
        maps = new ArrayList<>();
    }

    private void importData() {
        ImportFromFile importFromFile = new ImportFromFile();
        map = importFromFile.getDataAsStringBuilders(FILE);
        maps.add(map);
        maps.add(map);
        mapSize = map.size();
        lineSize = map.get(0).length();
    }


    @Override
    public void findSolution() {
        importData();
        int iteration = 0;
        while (iteration < 10) {
            for (int i = 0; i < mapSize; i++) {
                for (int j = 0; j < lineSize; j++) {
                    char charToCopy = maps.get(iteration % 2).get(i).charAt(j);
                    switch (charToCopy) {
                        case TREE:
                            charToCopy = (neighboursWith(LUMBERYARD, i, j,iteration) >= 3) ? LUMBERYARD : TREE;
                            break;
                        case OPEN:
                            charToCopy = (neighboursWith(TREE, i, j, iteration) >= 3) ? TREE : OPEN;
                            break;
                        case LUMBERYARD:
                            charToCopy = (neighboursWith(LUMBERYARD, i, j, iteration) > 0 &
                                    neighboursWith(TREE, i, j, iteration) >0) ? LUMBERYARD : OPEN;
                            break;
                    }
                    maps.get((iteration + 1) % 2).get(i).replace(j, j + 1, String.valueOf(charToCopy));
                }
            }
            iteration++;
            System.out.println("After " + iteration + " minutes, field will look like: ");
            for (StringBuilder line : maps.get(iteration % 2)) {
                System.out.println(line);
            }
        }
        int trees =0;
        int lumberyards = 0;
        for (StringBuilder line : maps.get(0)) {
            for (int i = 0; i < lineSize; i++) {
                if (line.charAt(i)==TREE) {
                    trees++;
                } else
                    if (line.charAt(i)==LUMBERYARD){
                        lumberyards++;
                    }
            }
        }
        System.out.println(trees*lumberyards);
    }

    private int neighboursWith(char what, int y, int x, int iteration) {
        int result = 0;
        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < 2; i++) {
                if (i*i+j*j>0 & y+i<mapSize & y+i>=0 & x+j<lineSize & x+j>=0){
                    if (maps.get(iteration % 2 ).get(y+i).charAt(x+j)==what)
                    {
                        result++;
                    }
                }
            }
        }
        return result;
    }
}
