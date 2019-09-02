package Day16;

import Intarface.Riddle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ChronalClassification implements Riddle {

    private static final String FILE = "./src/Day16/samples";
    private ArrayList<Sample> samples;
    private ArrayList<Opcode> opcodes;
    private int samplesWithAtLeast3possibilities;

    private void importData() {
        samples = new ArrayList<>();
        Integer[] registerBefore;
        Integer[] instructions;
        Integer[] registerAfter;
        setOpcodes();
        String line;
        try
                (FileReader fileReader = new FileReader(new File(FILE));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                registerBefore = fillRegister(line);
                line = bufferedReader.readLine();
                instructions = fillInstructions(line);
                line = bufferedReader.readLine();
                registerAfter = fillRegister(line);
                samples.add(new Sample(registerBefore, instructions, registerAfter));
                bufferedReader.readLine();
            }
        } catch (
                IOException e) {
            e.getMessage();
        }
    }

    private Integer[] fillInstructions(String line) {
        Integer[] result = new Integer[4];
        result[0] = Integer.valueOf(line.substring(0, line.length() - 6));
        result[1] = Integer.valueOf(line.substring(line.length() - 5, line.length() - 4));
        result[2] = Integer.valueOf(line.substring(line.length() - 3, line.length() - 2));
        result[3] = Integer.valueOf(line.substring(line.length() - 1));
        return result;
    }

    private Integer[] fillRegister(String line) {
        Integer[] result = new Integer[4];
        for (int i = 0; i < 4; i++) {
            result[i]=(Integer.valueOf(line.substring(9 + 3 * i, 10 + 3 * i)));
        }
        return result;
    }

    private void setOpcodes() {
        opcodes = new ArrayList<>();
        opcodes.add(new Opcode("addr", a -> a.getRegister()[a.getInstr()[1]] + a.getRegister()[a.getInstr()[2]]));
        opcodes.add(new Opcode("addi", a -> a.getRegister()[a.getInstr()[1]] + a.getInstr()[2]));
        opcodes.add(new Opcode("mulr", a -> a.getRegister()[a.getInstr()[1]] * a.getRegister()[a.getInstr()[2]]));
        opcodes.add(new Opcode("muli", a -> a.getRegister()[a.getInstr()[1]] * a.getInstr()[2]));
        opcodes.add(new Opcode("banr", a -> a.getRegister()[a.getInstr()[1]] & a.getRegister()[a.getInstr()[2]]));
        opcodes.add(new Opcode("bani", a -> a.getRegister()[a.getInstr()[1]] & a.getInstr()[2]));
        opcodes.add(new Opcode("borr", a -> a.getRegister()[a.getInstr()[1]] | a.getRegister()[a.getInstr()[2]]));
        opcodes.add(new Opcode("bori", a -> a.getRegister()[a.getInstr()[1]] | a.getInstr()[2]));
        opcodes.add(new Opcode("setr", a -> a.getRegister()[a.getInstr()[1]]));
        opcodes.add(new Opcode("seti", a -> a.getInstr()[1]));
        opcodes.add(new Opcode("gtir", a -> {
            if (a.getInstr()[1] > a.getRegister()[a.getInstr()[2]]) {
                return 1;
            } else return 0;
        }));
        opcodes.add(new Opcode("gtri", a -> {
            if (a.getRegister()[a.getInstr()[1]] > a.getInstr()[2]) {
                return 1;
            } else return 0;
        }));
        opcodes.add(new Opcode("gtrr", a -> {
            if (a.getRegister()[a.getInstr()[1]] > a.getRegister()[a.getInstr()[2]]) {
                return 1;
            } else return 0;
        }));
        opcodes.add(new Opcode("eqir", a -> {
            if (a.getInstr()[1].equals(a.getRegister()[a.getInstr()[2]])) {
                return 1;
            } else return 0;
        }));
        opcodes.add(new Opcode("eqri", a -> {
            if (a.getRegister()[a.getInstr()[1]].equals(a.getInstr()[2])) {
                return 1;
            } else return 0;
        }));
        opcodes.add(new Opcode("eqrr", a -> {
            if (a.getRegister()[a.getInstr()[1]].equals(a.getRegister()[a.getInstr()[2]])) {
                return 1;
            } else return 0;
        }));
    }

    @Override
    public void findSolution() {
        importData();
        System.out.println(howManySamples());
        assignNumbersToOpcodes();
    }

    private void assignNumbersToOpcodes() {
        ArrayList<Opcode> opcodesToWork = new ArrayList<>(opcodes);
        int discoveredNumber = 16;
        while (samples.size()>0) {
            for (Sample sample : samples) {
                int possibleOpcodes = 0;
                int id = 16;
                for (Opcode opcode : opcodesToWork) {
                    if (opcode.checkIfMatch(sample)){
                        possibleOpcodes++;
                        id = sample.getIdOfOpcode();
                        opcode.giveNumber(id);
                    }
                }
                if (possibleOpcodes==1){
                    discoveredNumber=id;
                    break;
                }
            }
            int finalDiscoveredNumber = discoveredNumber;
            samples.removeIf(sample -> sample.getIdOfOpcode()== finalDiscoveredNumber);
            opcodesToWork.removeIf(opcode -> opcode.getNumber() == finalDiscoveredNumber);
        }
    }

    private int howManySamples(){
        samplesWithAtLeast3possibilities=0;
        for (Sample sample : samples) {
            int possibleOpcodes = 0;
            for (Opcode opcode : opcodes) {
                if (opcode.checkIfMatch(sample)){
                    possibleOpcodes++;
                }
            }
            if (possibleOpcodes>=3){
                samplesWithAtLeast3possibilities++;
            }
        }
        return samplesWithAtLeast3possibilities;
    }
}
