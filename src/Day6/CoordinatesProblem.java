package Day6;

import Day4.Guard;
import ImportData.ImportFromFile;

import java.util.ArrayList;

public class CoordinatesProblem {
    private static final String FILE = "./src/Day6/locations";
    private static final int GRID_SIZE = 500;


    public void Coordinates() {
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        ArrayList<Place> locations = new ArrayList<>();
        for (String datum : data) {
            int x = Integer.parseInt(datum.substring(0, datum.indexOf(",") - 1));
            int y = Integer.parseInt(datum.substring(datum.indexOf(", ") + 2), datum.length());
            locations.add(new Place(x, y));
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
        for (Place location : locations) {
            System.out.println(location.area);
        }

    }

    private int[][] fillTheGrid(ArrayList<Place> locations) {
        //stwórz siatkę kartezjańską
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        //dla każdego punktu
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                // ustawa startową odległość na 1000 (nieskończoność)
                int distance = 2 * GRID_SIZE;
                // sprawdź wszystkie węzły
                for (Place place : locations) {
                    //jeśli odległość od danego węzła jest mniejsza niż dotychczasowa minimalna
                    if ((place.x + place.y) < distance) {
                        //zmień minimalny dystance dla tej lokacji na dystans do badanego węzła
                        distance = place.x + place.y;
                        //i ustaw w tym miejscu siatki liczbę określającą numer węzła do którego najbliżej
                        grid[i][j] = locations.indexOf(place);
                    } else if ((place.x + place.y) == distance) {
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
