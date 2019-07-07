package Day15;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.Comparator;

public class BeverageBandits implements Riddle {
    private static final String FILE = "./src/Day15/fightMap";
    private ArrayList<StringBuilder> map;
    private ArrayList<Creature> creatures;
    private int[][] distances;

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
        distances = new int[map.get(0).length()][map.size()];
        clearDistances();
    }

    private void clearDistances() {
        for (int[] distance : distances) {
            for (int i : distance) {
                distance[i]=1024;
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
            int currentDistance = 0;
            checkDistance(target.x, target.y,currentDistance);
            //TODO: wybierz z mapy DISTANCES sąsiadujące pola o najniższej wartości.
            clearDistances();

        }

        private void checkDistance(int x, int y, int currentDistance){
            if (map.get(y).charAt(x)=='.' && distances[x][y]>currentDistance) {
                distances[x][y]=currentDistance;
                checkDistance(x-1,y,currentDistance+1);
                checkDistance(x,y-1,currentDistance+1);
                checkDistance(x+1,y,currentDistance+1);
                checkDistance(x,y+1,currentDistance+1);
            }
        }

    }

    private enum Directions {
        LEFT, UP, RIGHT, DOWN
    }
}
