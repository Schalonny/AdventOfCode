package Day06;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.HashSet;

public class CoordinatesProblem implements Riddle {
    private static final String FILE = "./src/Day06/locations";
    private static final int MAX_SUMMARIZE_DISTANCE = 10000;
    private int minX = 1000, maxX = 0, minY = 1000, maxY = 0;
    private int areaOfHavingEveryCoordinateNearby = 0;
    private int maxAreaDominatedByOnePlace = 0;

    public void findSolution(){
        maxSaveArea();
        System.out.println();
        System.out.println("Max area where you are closer to one location than others is "
                + maxAreaDominatedByOnePlace + " feet square.");
        System.out.println("Area where you are closer than 10.000 feet from every location is "
                + areaOfHavingEveryCoordinateNearby + " feet square.");
    }


    private void maxSaveArea() {
        ArrayList<Place> locations = convertData();  //import węzłów oraz minXY maxXY
        int[][] grid = fillTheGridWithClosestIDs(locations);       //utwórz i wypełnij siatkę informacją do kogo jest najbliżej
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                // przejazd przez całą siatkę i zliczenie punktów do którego numeru jest najbliżej
                // (lokacja o danym numerze zwiększa powierzchnie)
                int neighbor = grid[x][y];
                if (neighbor != -1) {
                    locations.get(neighbor).area++;
                }
            }
        }
        // wywal ze zbioru lokacji zbanowane wartości (te które mają nieskończone pole)
        for (Integer banID : idsOfInfinity(grid)) {
            locations.remove(new Place(banID));
        }
        // z pozostałych mijesc znajdź wartość największego pola
        for (Place place : locations) {
            maxAreaDominatedByOnePlace = Math.max(maxAreaDominatedByOnePlace, place.area);
        }

    }

    private ArrayList<Place> convertData() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        ArrayList<Place> locations = new ArrayList<>();
        for (String datum : data) {
            int x = Integer.parseInt(datum.substring(0, datum.indexOf(",")));
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x + 1);
            int y = Integer.parseInt(datum.substring(datum.indexOf(", ") + 2));
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y + 1);
            // znajdujemy minimalne oraz maksymalne x i y i tylko w takim prostokącie bedziemy się poruszać
            // wszystko co będzie na skraju wtedy będzie miało pole nieskończoność
            locations.add(new Place(x, y, data.indexOf(datum)));
        }// przekształcenie danych z ArrayListy Stringów na ArrayListę współrzędnych

        return locations;
    }

    private HashSet<Integer> idsOfInfinity(int[][] grid) {
        HashSet<Integer> result = new HashSet<>();
        for (int y = minY; y < maxY; y++) {
            result.add(grid[minX][y]);
            result.add(grid[maxX - 1][y]);
        }
        for (int x = minX; x < maxX; x++) {
            result.add(grid[x][minY]);
            result.add(grid[x][maxY - 1]);
        }
        return result;
    }


    private int[][] fillTheGridWithClosestIDs(ArrayList<Place> locations) {
        //stwórz siatkę kartezjańską
        int[][] grid = new int[maxX][maxY];
        //dla każdego punktu
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                // ustawa startową odległość na 1000 (nieskończoność)
                int minDistance = maxX + maxY;
                // sprawdź wszystkie węzły
                int summaraizeDistanceToAllPlaces = 0;
                for (Place place : locations) {
                    //jeśli odległość od danego węzła jest mniejsza niż dotychczasowa minimalna
                    int distance = Math.abs(place.x - x) + Math.abs(place.y - y);
                    summaraizeDistanceToAllPlaces = summaraizeDistanceToAllPlaces + distance;
                    if (distance < minDistance) {
                        //zmień minimalny dystance dla tej lokacji na dystans do badanego węzła
                        minDistance = distance;
                        //i ustaw w tym miejscu siatki liczbę określającą numer węzła do którego najbliżej
                        grid[x][y] = place.index;
                    } else if (distance == minDistance) {
                        //w przeciwnym wypadku jeśli to pole zremisowało z innym najniższym (aktualnie)
                        //ustaw w tym węźle wartość -1 oznaczającą, że do nikogo nie ma "najbliżej"
                        grid[x][y] = -1;
                    }
                }
                if (summaraizeDistanceToAllPlaces < MAX_SUMMARIZE_DISTANCE) {
                    areaOfHavingEveryCoordinateNearby++;
                }
            }

        }
        return grid;
    }
}
