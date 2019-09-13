package Day16;

import java.util.ArrayList;
import java.util.function.Function;

public class Opcode {
    private String name;
    private Function<ArrayList<Integer[]>, Integer> function;
    //funkcja jako argumenty przyjmuje dwie tablice integerów
    //pierwsza tablica to dane wejściowe funkcji
    //druga tablica to operator i wskazania operandów
    //wynikiem jest liczba, przypisywana do odpowiedniego rekordu
    //(wynikiem mogłaby być tablica danych wyjściowych)
    private int number;


    Opcode(String name, Function<ArrayList<Integer[]>, Integer> function) {
        this.name = name;
        this.function = function;
        this.number = 16;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Opcode{" + name +
                ", " + number + '}';
    }

    boolean checkIfMatch(Sample sample) {
        int positionToCheck = sample.getInstr()[3];
        int predictedResult = sample.getRegisterAfter()[positionToCheck];
        ArrayList<Integer[]> test = new ArrayList<>();
        test.add(sample.getRegister());
        test.add(sample.getInstr());
        return predictedResult == function.apply(test);
    }

    void setNumber(int number) {
        this.number = number;
    }

    int getNumber() {
        return number;
    }

    public Integer[] execute(Integer[] input, Integer[] instruction) {
        ArrayList<Integer[]> functionData = new ArrayList<>();
        functionData.add(input);
        functionData.add(instruction);
        input[(int) instruction[3]] = function.apply(functionData);
        return input;
    }
}
