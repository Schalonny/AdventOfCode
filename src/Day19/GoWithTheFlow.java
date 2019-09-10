package Day19;

import Day16.ChronalClassification;
import Day16.Opcode;
import Intarface.Riddle;

import java.util.ArrayList;

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
}
