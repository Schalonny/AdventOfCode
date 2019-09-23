package Day03;

import Intarface.Riddle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FabricClaims implements Riddle {
    private static final String INPUT = "./src/Day03/fabricClaims";
    private static final int FABRIC_SIZE = 2000;

    @Override
    public void findSolution() {
        System.out.println("That many square inches has multiple claims: " + multipleClaims());
        System.out.println("Only claim that isn't overlap is claim of Elf no: " + untouchedClaim());

    }

    private int multipleClaims() {
        int squareInchesWithMultipleClaims = 0;
        int[][] fabric = claimsRead();
        for (int[] columnClaims : fabric) {
            for (int j = 0; j < fabric.length; j++) {
                if (columnClaims[j] > 1) squareInchesWithMultipleClaims++;
            }
        }
        return squareInchesWithMultipleClaims;
    }

    private int untouchedClaim() {
        int[][] fabric = claimsRead();
        String line;
        boolean isThisClaim;
        int unoverlapClaim = -1;
        try
                (FileReader fileReader = new FileReader(new File(INPUT));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                int startingXInch = Integer.parseInt(line.substring(line.indexOf(" @ ") + 3, line.indexOf(",")));
                int startingYInch = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(":")));
                int xLength = Integer.parseInt(line.substring(line.indexOf(": ") + 2, line.indexOf("x")));
                int yLength = Integer.parseInt(line.substring(line.indexOf("x") + 1));
                isThisClaim = true;
                for (int i = 0; i < xLength; i++) {
                    for (int j = 0; j < yLength; j++) {
                        if (fabric[i + startingXInch][j + startingYInch] != 1) {
                            isThisClaim = false;
                            break;
                        }
                    }
                }
                if (isThisClaim) {
                    unoverlapClaim = Integer.parseInt(line.substring(1, line.indexOf(" @ ")));
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return unoverlapClaim;
    }

    private int[][] claimsRead() {
        String line;
        int[][] fabric = new int[FABRIC_SIZE][FABRIC_SIZE];
        try
                (FileReader fileReader = new FileReader(new File(INPUT));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                int startingXInch = Integer.parseInt(line.substring(line.indexOf(" @ ") + 3, line.indexOf(",")));
                int startingYInch = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(":")));
                int xLength = Integer.parseInt(line.substring(line.indexOf(": ") + 2, line.indexOf("x")));
                int yLength = Integer.parseInt(line.substring(line.indexOf("x") + 1));
                for (int i = 0; i < xLength; i++) {
                    for (int j = 0; j < yLength; j++) {
                        fabric[i + startingXInch][j + startingYInch]++;
                    }
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return fabric;
    }
}
