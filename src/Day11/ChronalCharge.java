package Day11;

import Intarface.Riddle;

import java.util.ArrayList;
import java.util.Comparator;

public class ChronalCharge implements Riddle {
    private static final int GRID_SERIAL_NUMBER = 2694;
    private static final int GRID_SIZE = 300;
    private static final int RACK_ID_ADDITIONAL = 10 + 1;
    private int[][] grid;
    private ArrayList<Cell> sums;

    public ChronalCharge() {
        sums = new ArrayList<>();
        grid = new int[GRID_SIZE][GRID_SIZE];
    }

    @Override
    public void findSolution() {
        fillTheGrid();
        sums.sort(Comparator.comparing(Cell::getSum3x3).reversed());
        System.out.println(sums.get(0).x+1 + "," + sums.get(0).y+1);
        //numeracja w zadaniu jest od 1 do 300, zamiast od 0, stąd "+1"
        sums.sort(Comparator.comparing(Cell::getMaxSum).reversed());
        System.out.println(sums.get(0).maxSum);
        System.out.println(sums.get(0).x+1 + "," + sums.get(0).y+1 + "," + sums.get(0).sizeOfMaxSumSquare);

    }

    private void fillTheGrid() {
        for (int x = GRID_SIZE - 1; x >= 0; x--) {
            for (int y = GRID_SIZE - 1; y >= 0; y--) {
                int rackId = x + RACK_ID_ADDITIONAL;
                grid[x][y] = (((rackId * (y + 1)
                        + GRID_SERIAL_NUMBER) * rackId) / 100) % 10 - 5;

                // dla każdego pola na siatce wykonaj obliczenia zadane w treści
                calculateSquaresValues(x, y);
                // oblicz wartości dla tej komórki
            }
        }
    }

    private void calculateSquaresValues(int x, int y) {
        int sum3x3 = 0;
        if (y < GRID_SIZE - 3 && x < GRID_SIZE - 3) {
            // jeśli jesteśmy wystarczajaco daleko od krawędzi by zmieścić kwadrat 3x3
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    sum3x3 += grid[x + i][y + j];
                    // jako sumę ustaw zsumowaną wartość tych 9 liczb z kwadratu
                }
            }
        }
        sums.add(setMaxSumParameters(new Cell(x, y, sum3x3)));
    }

    private Cell setMaxSumParameters(Cell cell){
        cell.maxSum = cell.sum3x3;
        // zakładamy, że dla danej komórki jej największą watrtością jest wartość z kwadratu 3x3
        // co nie musi być koniecznie prawdą, ale interesują nas znacznie większe wyniki
        for (int sizeOfSquare=4; sizeOfSquare<GRID_SIZE-cell.x && sizeOfSquare<GRID_SIZE-cell.y; sizeOfSquare++){
            // dopóki wielkość sprawdzanego kwadratu pozostaje w wymiarach siatki
            int sum = 0;
            for (int x = 0; x < sizeOfSquare; x++) {
                for (int y = 0; y < sizeOfSquare; y++) {
                    sum += grid[cell.x + x][cell.y + y];
                    //zsumuj wszystkie wartości z badanego kwadratu
                }
            }
            if (sum>cell.maxSum){
                // jeśli obliczona suma jest większa niż dotychczasowa maxSum
                cell.sizeOfMaxSumSquare = sizeOfSquare;
                cell.maxSum = sum;
                // to odpowiedniu ustaw wartości
            }
        }
        return cell;
    }

    private class Cell {
        int x, y, sum3x3;
        int maxSum, sizeOfMaxSumSquare;

        Cell(int x, int y, int sum3x3) {
            this.x = x;
            this.y = y;
            this.sum3x3 = sum3x3;
            this.maxSum = sum3x3;
            sizeOfMaxSumSquare = 3;
        }

        int getSum3x3() {
            return sum3x3;
        }

        int getMaxSum() {
            return maxSum;
        }
    }
}
