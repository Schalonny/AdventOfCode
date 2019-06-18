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
        sums.sort(Cell::compareTo);
        System.out.println(sums.get(0).x + "," + sums.get(0).y);
       /* sums.sort(Comparator.comparing(Cell::getMaxSum).reversed());
        for (int i = 0; i < 1; i++) {
            System.out.println(sums.get(i).maxSum);
            System.out.println(sums.get(i).x + "," + sums.get(i).y + "," + sums.get(i).sizeOfMaxSum);
        }*/
    }

    private void fillTheGrid() {
        for (int x = GRID_SIZE - 1; x >= 0; x--) {
            for (int y = GRID_SIZE - 1; y >= 0; y--) {
                int rackId = x + RACK_ID_ADDITIONAL;
                grid[x][y] = (((rackId * (y + 1)
                        + GRID_SERIAL_NUMBER) * rackId) / 100) % 10 - 5;
                calculateSquaresValues(x, y);
            }
        }
    }

    private void calculateSquaresValues(int x, int y) {
        int sum = 0;
        if (y < GRID_SIZE - 3 && x < GRID_SIZE - 3) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    sum += grid[x + i][y + j];
                }
            }
        }
        Cell cell = new Cell(x, y, sum);
        int squareSize = 0;
        cell.maxSum = -5;
        while (x + squareSize < GRID_SIZE && y + squareSize < GRID_SIZE && squareSize < 20) {
            squareSize++;
            int temporarySum = 0;
            for (int i = 0; i < squareSize; i++) {
                for (int j = 0; j < squareSize; j++) {
                    temporarySum += grid[x + i][y + j];
                }
            }
            if (cell.maxSum < temporarySum) {
                cell.maxSum = temporarySum;
                cell.sizeOfMaxSum = squareSize;
            }
        }
        sums.add(cell);
    }

    private class Cell implements Comparable<Cell> {
        int x, y, sum3x3, maxSum, sizeOfMaxSum;

        public int getMaxSum() {
            return maxSum;
        }

        public Cell(int x, int y, int sum3x3) {
            this.x = x;
            this.y = y;
            this.sum3x3 = sum3x3;
        }

        @Override
        public int compareTo(Cell o) {
            return o.sum3x3 - this.sum3x3;
        }
    }
}
