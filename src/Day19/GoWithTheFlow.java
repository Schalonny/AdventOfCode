package Day19;

import Day16.ChronalClassification;
import Day16.Opcode;
import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GoWithTheFlow implements Riddle {
    private static final String FILE = "./src/Day19/instructions";
    private ArrayList<Integer[]> instructions;
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
            Integer[] instrInt = new Integer[5];
            String[] instrStr = instruction.split(" ");
            instrInt[0] = data.indexOf(instruction)==0 ? opcodes.indexOf(findOpcodeByName(instrStr[0])) : 0;
            for (int i = 1; i < instrStr.length; i++) {
                instrInt[i] = Integer.parseInt(instrStr[i]);
            }
            instructions.add(instrInt);
        }
    }

    @Override
    public void findSolution() {
        int positionToModify = instructions.get(0)[1];
        instructions.remove(0);
        int marker = 0;
        while (marker<instructions.size()){
            register = opcodes.get(instructions.get(marker)[0]).execute(register, instructions.get(marker));
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
