package Day16;

class Sample {
    private Integer[] register;
    private Integer[] instr;
    private Integer[] registerAfter;

    Sample(Integer[] registerBefore, Integer[] instructions, Integer[] registerAfter) {
        this.register = registerBefore;
        this.instr = instructions;
        this.registerAfter = registerAfter;
    }

    public int getIdOfOpcode(){
        return this.instr[0];
    }

    public Integer[] getRegister() {
        return register;
    }

    public Integer[] getInstr() {
        return instr;
    }

    public Integer[] getRegisterAfter() {
        return registerAfter;
    }
}