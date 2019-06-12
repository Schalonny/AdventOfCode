package Day6;

import Day4.Guard;
import ImportData.ImportFromFile;

import java.util.ArrayList;
import java.util.HashSet;

public class CoordinatesProblem {
    private static final String FILE = "./src/Day6/locations";
    private static final int GRID_SIZE = 400;


    public void Coordinates() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        ArrayList<Place> locations = new ArrayList<>();
        for (String datum : data) {
            int x = Integer.parseInt(datum.substring(0, datum.indexOf(",")));
            int y = Integer.parseInt(datum.substring(datum.indexOf(", ") + 2));
            // można znaleźć minimalne oraz maksymalne x i y i tylko w takim kwadracie się poruszać
            // wszystko co będzie na skraju wtedy będzie miało pole nieskończoność
            // TODO
            locations.add(new Place(x, y, data.indexOf(datum)));
        }// przekształcenie danych z ArrayListy Stringów na ArrayListę współrzędnych
        // locations(0), locations(1) ... to kolejne węzły

        //utwórz i wypełnij siatkę informacją do kogo jest najbliżej
        int[][] grid = fillTheGrid(locations);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                // przejazd przez całą siatkę i zliczenie punktów (lokacja o danym numerze zwiększa powierzchnie
                int neighbor = grid[i][j];
                if (neighbor != -1) {
                    locations.get(neighbor).area++;
                }
            }
        }
        // wywal ze zbioru lokacji zbanowane wartości (te które mają nieskończone pole)
        for (Integer banID : idsOfInfinity(grid)) {
            locations.remove(new Place(banID));
        }
        int maxArea = 0;
        //  int maxAreaLocation = -1;
        for (Place place : locations) {
            maxArea = Math.max(maxArea,place.area);
        }

        System.out.println(maxArea);


    }

    private HashSet<Integer> idsOfInfinity(int[][] grid) {
        HashSet<Integer> result = new HashSet<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            result.add(grid[0][i]);
            result.add(grid[GRID_SIZE - 1][i]);
            result.add(grid[i][0]);
            result.add(grid[i][GRID_SIZE - 1]);
        }
        return result;
    }


    private int[][] fillTheGrid(ArrayList<Place> locations) {
        //stwórz siatkę kartezjańską
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        //dla każdego punktu
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                // ustawa startową odległość na 1000 (nieskończoność)
                int minDistance = 2 * GRID_SIZE;
                // sprawdź wszystkie węzły
                for (Place place : locations) {
                    //jeśli odległość od danego węzła jest mniejsza niż dotychczasowa minimalna
                    int distance = Math.abs(place.x - i) + Math.abs(place.y - j);
                    if (distance < minDistance) {
                        //zmień minimalny dystance dla tej lokacji na dystans do badanego węzła
                        minDistance = distance;
                        //i ustaw w tym miejscu siatki liczbę określającą numer węzła do którego najbliżej
                        grid[i][j] = place.index;
                    } else if (distance == minDistance) {
                        //w przeciwnym wypadku jeśli to pole zremisowało z innym najniższym (aktualnie)
                        //ustaw w tym węźle wartość -1 oznaczającą, że do nikogo nie ma "najbliżej"
                        grid[i][j] = -1;
                    }
                }
            }

        }
        return grid;
    }
}
