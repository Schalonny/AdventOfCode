package Day04;

import java.util.Arrays;
import java.util.Objects;

public class Guard implements Comparable<Guard> {
    private int number;
    private int[] sleep = new int[60]; //default - 0
    private int shifts;
    private int totalAsleep;

    public Guard(int number) {
        this.number = number;
        this.shifts = 0;
        this.totalAsleep = 0;
    }

    void increaseShiftsByOne() {
        this.shifts++;
    }

    int[] getSleep() {
        return this.sleep;
    }

    int getNumber() {
        return this.number;
    }

    int getTotalAsleep() {
        return this.totalAsleep;
    }

    void putToSleep(int minute) {
        this.sleep[minute]++;
        this.totalAsleep++;
    }

    void awake(int minute) {
        this.sleep[minute]--;
        this.totalAsleep--;
    }

    @Override
    public String toString() {
        return "Guard{" +
                "number=" + number +
                ", sleep=" + Arrays.toString(sleep) +
                ", shifts=" + shifts +
                ", total sleeps=" + totalAsleep +
                '}' + '\n';
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
        return Objects.hash(number);
    }
}
