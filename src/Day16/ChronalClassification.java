package Day16;

import Intarface.Riddle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ChronalClassification implements Riddle {

    private static final String FILE = "./src/Day16/samples";
    private static final String FILE_TO_EXECUTE = "./src/Day16/testProgram";
    private ArrayList<Sample> samples;
    private ArrayList<Opcode> opcodes;
    private ArrayList<Integer[]> testProgram;

    public ChronalClassification(){
        opcodes = setOpcodes();
    }
    
    private void importData() {
        samples = new ArrayList<>();
        String line;
        try
                (FileReader fileReader = new FileReader(new File(FILE));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                samples.add(new Sample(
                        setRegister(line),
                        setInstructions(bufferedReader.readLine()),
                        setRegister(bufferedReader.readLine())));
                bufferedReader.readLine();
            }
        } catch (
                IOException e) {
            e.getMessage();
        }
        testProgram = new ArrayList<>();
        try
                (FileReader fileReader = new FileReader(new File(FILE_TO_EXECUTE));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                testProgram.add(setInstructions(line));
            }
        } catch (
                IOException e) {
            e.getMessage();
        }
    }

    private Integer[] setInstructions(String line) {
        Integer[] result = new Integer[4];
        result[0] = Integer.valueOf(line.substring(0, line.length() - 6));
        result[1] = Integer.valueOf(line.substring(line.length() - 5, line.length() - 4));
        result[2] = Integer.valueOf(line.substring(line.length() - 3, line.length() - 2));
        result[3] = Integer.valueOf(line.substring(line.length() - 1));
        return result;
    }

    private Integer[] setRegister(String line) {
        Integer[] result = new Integer[4];
        for (int i = 0; i < 4; i++) {
            result[i] = (Integer.valueOf(line.substring(9 + 3 * i, 10 + 3 * i)));
        }
        return result;
    }

    public ArrayList<Opcode> setOpcodes() {
        ArrayList<Opcode> result = new ArrayList<>();
        //a.get(0) - zawiera register wejściowy [0 0 0 0]
        //a.get(1) - zawiera operator i wskazuje operandy [name A B C]
        result.add(new Opcode("addr", a -> a.get(0)[a.get(1)[1]] + a.get(0)[a.get(1)[2]]));
        result.add(new Opcode("addi", a -> a.get(0)[a.get(1)[1]] + a.get(1)[2]));
        result.add(new Opcode("mulr", a -> a.get(0)[a.get(1)[1]] * a.get(0)[a.get(1)[2]]));
        result.add(new Opcode("muli", a -> a.get(0)[a.get(1)[1]] * a.get(1)[2]));
        result.add(new Opcode("banr", a -> a.get(0)[a.get(1)[1]] & a.get(0)[a.get(1)[2]]));
        result.add(new Opcode("bani", a -> a.get(0)[a.get(1)[1]] & a.get(1)[2]));
        result.add(new Opcode("borr", a -> a.get(0)[a.get(1)[1]] | a.get(0)[a.get(1)[2]]));
        result.add(new Opcode("bori", a -> a.get(0)[a.get(1)[1]] | a.get(1)[2]));
        result.add(new Opcode("setr", a -> a.get(0)[a.get(1)[1]]));
        result.add(new Opcode("seti", a -> a.get(1)[1]));
        result.add(new Opcode("gtir", a -> boolToInt(a.get(1)[1] > a.get(0)[a.get(1)[2]])));
        result.add(new Opcode("gtri", a -> boolToInt(a.get(0)[a.get(1)[1]] > a.get(1)[2])));
        result.add(new Opcode("gtrr", a -> boolToInt(a.get(0)[a.get(1)[1]] > a.get(0)[a.get(1)[2]])));
        result.add(new Opcode("eqir", a -> boolToInt(a.get(1)[1].equals(a.get(0)[a.get(1)[2]]))));
        result.add(new Opcode("eqri", a -> boolToInt(a.get(0)[a.get(1)[1]].equals(a.get(1)[2]))));
        result.add(new Opcode("eqrr", a -> boolToInt(a.get(0)[a.get(1)[1]].equals(a.get(0)[a.get(1)[2]]))));
        return result;
    }

    private int boolToInt(boolean statement) {
        return statement ? 1 : 0;
    }

    @Override
    public void findSolution() {
        importData();
        System.out.println(howManySamples() + " samples behave like 3 or more opcodes.");
        assignNumbersToOpcodes();
        opcodes.sort(Comparator.comparingInt(Opcode::getNumber));
        Integer[] workingRegister = new Integer[]{0,0,0,0};
        for (Integer[] instruction : testProgram) {
            workingRegister = opcodes.get(instruction[0]).execute(workingRegister, instruction);
        }
        System.out.println("Last register will start from: " + workingRegister[0]);
    }

    private void assignNumbersToOpcodes() {
        ArrayList<Opcode> opcodesToWork = new ArrayList<>(opcodes);
        int discoveredNumber = 16;
        while (samples.size() > 0) {
            for (Sample sample : samples) {
                int possibleOpcodes = 0;
                int id = 16;
                for (Opcode opcode : opcodesToWork) {
                    if (opcode.checkIfMatch(sample)) {
                        possibleOpcodes++;
                        id = sample.getIdOfOpcode();
                        opcode.setNumber(id);
                    }
                }
                if (possibleOpcodes == 1) {
                    discoveredNumber = id;
                    break;
                }
            }
            int finalDiscoveredNumber = discoveredNumber;
            samples.removeIf(sample -> sample.getIdOfOpcode() == finalDiscoveredNumber);
            opcodesToWork.removeIf(opcode -> opcode.getNumber() == finalDiscoveredNumber);
        }
    }

    private int howManySamples() {
        int samplesWithAtLeast3possibilities = 0;
        for (Sample sample : samples) {
            int possibleOpcodes = 0;
            for (Opcode opcode : opcodes) {
                if (opcode.checkIfMatch(sample)) {
                    possibleOpcodes++;
                }
            }
            if (possibleOpcodes >= 3) {
                samplesWithAtLeast3possibilities++;
            }
        }
        return samplesWithAtLeast3possibilities;
    }
}
