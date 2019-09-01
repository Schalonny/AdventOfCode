package Day15;

class Coordinates implements Comparable<Coordinates> {
   private int x;
   private int y;

    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public int compareTo(Coordinates o) {
        return 100 * (this.y - o.y) + (this.x - o.x);
    }
    // Ta klasa ma za zadanie porównywać pozycję stworzeń by rozstrzygać remisy, zgodnie z treścią
    // zadania, jeśli mogę wybrać klika opcji, powinniśmy wybierać poczynając od tego który jest wyżej,
    // a jeśli są na tym samym poziomie to tego, który jest z lewej.
}
