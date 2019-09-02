package Day16;

import java.util.function.Function;

class Opcode {
    private String name;
    private Function<Sample,Integer> function;
    private int number;

    Opcode(String name, Function<Sample, Integer> function) {
        this.name = name;
        this.function = function;
        this.number = 16;
    }

    @Override
    public String toString() {
        return "Opcode{" + name +
                ", " + number + '}';
    }

    Integer[] execute (Sample input){
        int positionToChange = input.getInstr()[3];
        Integer[] data = input.getRegister();
        data[positionToChange]=function.apply(input);
        return data;
    }

    boolean checkIfMatch(Sample sample){
        int positionToCheck = sample.getInstr()[3];
        int predictedResult = sample.getRegisterAfter()[positionToCheck];
        return predictedResult==function.apply(sample);
    }

    void giveNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }
}
