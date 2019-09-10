package Day16;

import java.util.function.Function;

public class Opcode {
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

    Integer[] executeAndProduceNewRegister(Sample input){
        Integer[] data = input.getRegister();
        data[input.getInstr()[3]] = function.apply(input);
        return data;
    }

    boolean checkIfMatch(Sample sample){
        int positionToCheck = sample.getInstr()[3];
        int predictedResult = sample.getRegisterAfter()[positionToCheck];
        return predictedResult == function.apply(sample);
    }

    void setNumber(int number) {
        this.number = number;
    }

   int getNumber() {
        return number;
    }
}
