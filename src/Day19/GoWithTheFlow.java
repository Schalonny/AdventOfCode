package Day19;

import Day16.ChronalClassification;
import Day16.Opcode;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GoWithTheFlow implements Riddle {
    private ArrayList<Opcode> opcodes;
    private Integer[] register;

    GoWithTheFlow(){
        opcodes = new ChronalClassification().setOpcodes();
        register = new Integer[] {0,0,0,0,0,0};
    }
    @Override
    public void findSolution() {
    }

    Opcode findOpcodeByName(String name){
        return opcodes.stream().filter(op -> op.getName().equals(name)).collect(Collectors.toList()).get(0);
    }
}
