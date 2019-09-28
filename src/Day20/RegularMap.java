package Day20;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.HashMap;

public class RegularMap implements Riddle {
    private static final String FILE = "./src/Day20/instructions";
    private static String instructions;
    private HashMap<String, Coordinate> map;
    private Coordinate currentLocation;
    private int currentChar;

    private void importData() {
        instructions = new ImportFromFile().getData(FILE).get(0);
    }

    public RegularMap() {
        importData();
        map = new HashMap<>();
        currentLocation = new Coordinate(0, 0, 0);
        map.put(currentLocation.id(), currentLocation);
        currentChar = 0;
    }

    @Override
    public void findSolution() {
        goWithPath(currentLocation);
    }

    private int goWithPath(Coordinate currentLocation) {
        currentChar++;
        while (instructions.charAt(currentChar) != ')') {
            while (instructions.charAt(currentChar) != '|') {
                if (instructions.charAt(currentChar) == '(') {
                    goWithPath(currentLocation);
                } else {
                    int x = currentLocation.getX();
                    int y = currentLocation.getY();
                    switch (instructions.charAt(currentChar)) {
                        case 'N':
                            y--;
                            break;
                        case 'E':
                            x++;
                            break;
                        case 'W':
                            x--;
                            break;
                        case 'S':
                            y++;
                            break;
                    }
                    String key = x + "," + y;
                    map.containsKey(key) ? checkDistance(map.get(key), currentLocation.getDistance()+1) :
                            map.put(key, new Coordinate(x, y, currentLocation.getDistance()+1));
                }
            }
        }
        return
    }
}
