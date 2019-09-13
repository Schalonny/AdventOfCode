package Day19;

import Day16.ChronalClassification;
import Day16.Opcode;
import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GoWithTheFlow implements Riddle {
    private static final String FILE = "./src/Day19/instructions";
    private ArrayList<Serializable[]> instructions;
    private ArrayList<Opcode> opcodes;
    private Integer[] register;

    public GoWithTheFlow(){
        importData();
    }

    void importData(){
        opcodes = new ChronalClassification().setOpcodes();
        register = new Integer[] {1,0,0,0,0,0};
        ArrayList<String> data = new ImportFromFile().getData(FILE);
        instructions = new ArrayList<>();
        for (String instruction : data) {
            instructions.add(instruction.split(" "));
        }
    }

    @Override
    public void findSolution() {
        int positionToModify = -1;
        int marker = 0;
        while (marker<instructions.size()){
            if (instructions.get(marker).length==2){
                positionToModify = Integer.parseInt((String) instructions.get(marker)[1]);
                instructions.remove(marker);
                continue;
            }
            Integer[] intInstruction = new Integer[4];
            intInstruction[0] = 0;
            for (int i = 1; i < intInstruction.length; i++) {
                intInstruction[i] = Integer.parseInt((String) instructions.get(marker)[i]);
            }
            String opcodeName = (String) instructions.get(marker)[0];
            register = findOpcodeByName(opcodeName).execute(register, intInstruction);
            register[positionToModify]++;
            marker = register[positionToModify];
            System.out.println(Arrays.toString(register));
        }
        System.out.println(register[0]);
    }

    private Opcode findOpcodeByName(String name){
        return opcodes.stream().filter(op -> op.getName().equals(name)).collect(Collectors.toList()).get(0);
    }
}
