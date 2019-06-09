package Day4;

import java.util.Arrays;
import java.util.Objects;

public class Guard implements Comparable<Guard> {
    private int number;
    private boolean[] sleep = new boolean[60]; //default - false
    private int shifts;

    public Guard(int number) {
        this.number = number;
        this.shifts = 0;
    }

    public int getNumber() {
        return number;
    }

    public int getShifts() {
        return shifts;
    }

    public void setShifts(int shifts) {
        this.shifts = shifts;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean[] getSleep() {
        return sleep;
    }

    public void setSleep(boolean[] sleep) {
        this.sleep = sleep;
    }

    public void putToSleep(int minute){
        this.sleep[minute]=true;
    }

    @Override
    public int compareTo(Guard o) {
        return o.number - this.number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guard guard = (Guard) o;
        return number == guard.number;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(number, shifts);
        result = 31 * result + Arrays.hashCode(sleep);
        return result;
    }
}
