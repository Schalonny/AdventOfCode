package Day15;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.Comparator;

public class BeverageBandits implements Riddle {
    private static final String FILE = "./src/Day15/fightMap";
    private ArrayList<StringBuilder> map;
    private ArrayList<Creature> creatures;

    public BeverageBandits() {
        ImportFromFile importFromFile = new ImportFromFile();
        map = importFromFile.getDataAsStringBuilders(FILE);
        creatures = new ArrayList<>();
        for (StringBuilder line : map) {
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'E') {
                    creatures.add(new Creature(true, x, map.indexOf(line)));
                } else if (line.charAt(x) == 'G') {
                    creatures.add(new Creature(false, x, map.indexOf(line)));
                }
            }
        }
    }

    @Override
    public void findSolution() {

    }

    private void sortCreatures() {
        creatures.sort(Comparator.comparingInt(c -> c.y)); //TODO sortuje tylko po y, jak są posortowane x?
    }


    private class Creature {
        boolean isElf;
        boolean isDead;
        int x;
        int y;
        int hp;
        int power;

        Creature(boolean isElf, int x, int y) {
            this.isElf = isElf;
            this.isDead = false;
            this.x = x;
            this.y = y;
            this.hp = 200;
            this.power = 3;
        }

        void move(Directions direction) {
            map.get(y).replace(x, x + 1, ".");
            switch (direction) {
                case LEFT:
                    this.x--;
                    break;
                case RIGHT:
                    this.x++;
                    break;
                case UP:
                    this.y--;
                    break;
                case DOWN:
                    this.y++;
                    break;
            }
            map.get(y).replace(x, x + 1, creatureSymbol());
        }

        String creatureSymbol() {
            if (this.isElf) {
                return "E";
            } else return "G";
        }

        void loseLife(int damage) {
            this.hp -= damage;
            if (this.hp <= 0) {
                this.isDead = true;
            }
        }

        void calculateDistance(Creature target){

        }

    }

    private enum Directions {
        LEFT, UP, RIGHT, DOWN
    }
}
