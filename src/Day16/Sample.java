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

    Sample(Integer[] instructions){
        this.register = instructions;
        this.instr = instructions;
        this.registerAfter = instructions;
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

    public void setRegister(Integer[] register) {
        this.register = register;
    }

    public void setInstr(Integer[] instr) {
        this.instr = instr;
    }

    public void setRegisterAfter(Integer[] data) {
        this.registerAfter = data;
    }
}