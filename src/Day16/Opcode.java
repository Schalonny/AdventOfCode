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

    Integer[] execute (Integer[] input){
        //TODO
        return input;
    }

    boolean checkIfMatch(Sample sample){
        return sample.getRegisterAfter()[sample.getInstr()[3]].equals(function.apply(sample));
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
