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
        sums.add(setMaxSumParameters(new Cell(x, y, sum)));
    }

    private Cell setMaxSumParameters(Cell cell){
        cell.maxSum = cell.sum3x3;
        for (int i=4; i<GRID_SIZE-cell.x && i<GRID_SIZE-cell.y; i++){
            int sum = 0;
            for (int x = 0; x < i; x++) {
                for (int y = 0; y < i; y++) {
                    sum += grid[cell.x + x][cell.y + y];
                }
            }
            if (sum>cell.maxSum){
                cell.sizeOfMaxSumSquare = i;
                cell.maxSum = sum;
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
