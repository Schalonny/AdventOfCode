package Day14;

import Intarface.Riddle;

public class ChocolateCharts implements Riddle {
    private int numberOfRecipes;
    private Recipe firstRecipe, lastRecipe;
    private Recipe firstElf;
    private Recipe secondElf;
    private final static int ITERATIONS = 293801;
    private final static int RESULT_LENGTH = 10;

    public ChocolateCharts() {
        lastRecipe = new Recipe();
        numberOfRecipes = 0;
        firstRecipe = new Recipe(3);
        addRecipes(7);
        firstElf = firstRecipe;
        secondElf = lastRecipe;
    }

    @Override
    public void findSolution() {
        while (numberOfRecipes<ITERATIONS+RESULT_LENGTH+1){
            addRecipes(firstElf.score + secondElf.score);
            elvesTakesNewRecipes();
        }

    }

    private void addRecipes(int valueOfRecipe) {
        if (valueOfRecipe < 10) {
            new Recipe(valueOfRecipe);
        } else {
            int decimal, units;
            decimal = valueOfRecipe / 10;
            units = valueOfRecipe % 10;
            new Recipe(decimal);
            new Recipe(units);
        }
    }

    private void elvesTakesNewRecipes(){
        int firstElfJump = firstElf.score+1;
        int secondElfJump = secondElf.score+1;
        for (int i=0; i<firstElfJump; i++){
            firstElf= firstElf.nextRecipe;
        }
        for (int j=0; j<secondElfJump; j++){
            secondElf = secondElf.nextRecipe;
        }
    }

    private class Recipe {
        int score;
        Recipe nextRecipe;

        Recipe() {}

        Recipe(int newValue) {
            this.score = newValue;
            lastRecipe.nextRecipe = this;
            lastRecipe = this;
            this.nextRecipe = firstRecipe;
            numberOfRecipes++;
            if (numberOfRecipes>ITERATIONS) {
                System.out.print(this.score);
            }
        }
    }
}