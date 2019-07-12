package Day15;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.Comparator;

public class BeverageBandits implements Riddle {
    private static final String FILE = "./src/Day15/fightMap";
    private static final int INFINITY = 1024;
    private static final char EMPTY_CHAR = '.';
    private static final char ELF_CHAR = 'E';
    private static final char GOBLIN_CHAR = 'G';
    private ArrayList<StringBuilder> map;
    private ArrayList<Creature> creatures;
    private int[][] distances;
    private int numberOfElves = 0;
    private int numberOfGoblins = 0;

    public BeverageBandits() {
        ImportFromFile importFromFile = new ImportFromFile();
        map = importFromFile.getDataAsStringBuilders(FILE);
        creatures = new ArrayList<>();
        for (StringBuilder line : map) {
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == ELF_CHAR) {
                    creatures.add(new Creature(true, x, map.indexOf(line)));
                    numberOfElves++;
                } else if (line.charAt(x) == GOBLIN_CHAR) {
                    creatures.add(new Creature(false, x, map.indexOf(line)));
                    numberOfGoblins++;
                }
            }
        }
        distances = new int[map.get(0).length()][map.size()];
        clearDistances();
    }

    private void clearDistances() {
        for (int[] distance : distances) {
            for (int i : distance) {
                distance[i] = INFINITY;
            }
        }
    }

    @Override
    public void findSolution() {
        int rounds = 0;
        Directions attackDirection;
        while (numberOfElves > 0 && numberOfGoblins > 0) {
            sortCreatures();
            for (Creature currentCreature : creatures) {
                attackDirection = currentCreature.whereIsAdjacentEnemy();
                if (attackDirection != Directions.NONE) {
                    currentCreature.attack(attackDirection);
                    if (numberOfElves * numberOfGoblins == 0) {
                        break;
                    }
                } else {
                    ArrayList<Integer[]> targetCoordinates = new ArrayList<>();
                    creatures.stream()
                            .filter(c -> c.isElf != currentCreature.isElf) //find all enemies
                            .forEach(c -> targetCoordinates.addAll(getNeighbours(c.x, c.y, EMPTY_CHAR)));
                    targetCoordinates.sort //TODO sort by reading order
                    int minDistance = INFINITY;
                    for (Integer[] target : targetCoordinates) {
                        minDistance = calculateDistanceTo(target[0], target[1], currentCreature, minDistance);
                    }
                    //TODO if scope above will go by reading order we are ready to go!
                    if (minDistance < INFINITY) {
                        currentCreature.move(currentCreature.toMove);
                    }
                }
                rounds++;
            }
        }
        int sumOfHP = 0;
        for (Creature creature : creatures) {
            if (!creature.isDead) {
                sumOfHP += creature.hp;
            }
        }

        System.out.println("After " + rounds + " rounds, the control number is " + sumOfHP * rounds);
    }

    Creature findCreatureByLocation(int x, int y) {
        for (Creature creature : creatures) {
            if (creature.x == x && creature.y == y) {
                return creature;
            }
        }
        System.out.println("Creature not found!");
        //here can be try/catch but it will never occur
        return null;
    }

    ArrayList<Integer[]> getNeighbours(int x, int y, char what) {
        ArrayList<Integer[]> result = new ArrayList<>();
        Integer[] pair = new Integer[2];
        for (int i = 0; i < 4; i++) {
            pair[0] = x + (i % 2 * (i - 2)); // 0, -1, 0, +1
            pair[1] = y + ((i + 1) % 2 * (i - 1)); // -1, 0 , +1 , 0
            //TODO:
            // x = 0, -1, +1, 0 <- this order will be great, can optimize rest of code
            // y = -1, 0, 0, +1
            // we can change attack concept to attack(getNeighbours(x,y,ENEMY_CHAR).get(0))


            if (map.get(pair[1]).charAt(pair[0]) == what) {
                result.add(pair);
            }
        }
        return result; // return list of spaces adjacent to (x,y) contains "what"
    }


    private void sortCreatures() {
        creatures.sort(Comparator.comparingInt(c -> c.y * INFINITY + c.x));
    }

    private int calculateDistanceTo(int x, int y, Creature attackingCreature, int minDistance) {
        if (getNeighbours(attackingCreature.x, attackingCreature.y, EMPTY_CHAR).size() != 0) {
            int currentDistance = 0;
            checkDistance(x, y, currentDistance);
            for (Integer[] neighbour : getNeighbours(attackingCreature.x, attackingCreature.y, EMPTY_CHAR)) {
                if (distances[neighbour[0]][neighbour[1]] < minDistance) {
                    minDistance = distances[neighbour[0]][neighbour[1]];
                    //TODO: steal its wrong order (non reading order) - issue is in getNeighbour order of neighbours
                    attackingCreature.toMove = getDirection(attackingCreature.x, attackingCreature.y, neighbour[0], neighbour[1]);
                }
            }
            clearDistances();
        }
        return minDistance;
    }

    private void checkDistance(int x, int y, int currentDistance) {
        if (map.get(y).charAt(x) == EMPTY_CHAR && currentDistance < distances[x][y]) {
            //won't be out of boundary, becouse there is # all around
            distances[x][y] = currentDistance;
            currentDistance++;
            checkDistance(x - 1, y, currentDistance);
            checkDistance(x, y - 1, currentDistance);
            checkDistance(x + 1, y, currentDistance);
            checkDistance(x, y + 1, currentDistance);
        }
    }

    private Directions getDirection(int fromX, int fromY, int toX, int toY) {
        if (fromX > toX) {
            return Directions.LEFT;
        } else if (fromX < toX) {
            return Directions.RIGHT;
        } else if (fromY > toY) {
            return Directions.UP;
        } else if (fromY < toY) {
            return Directions.DOWN;
        }
        return Directions.NONE; //never occur
    }

    private class Creature {
        boolean isElf;
        boolean isDead;
        int x;
        int y;
        int hp;
        int power;
        Directions toMove;

        Creature(boolean isElf, int x, int y) {
            this.isElf = isElf;
            this.isDead = false;
            this.x = x;
            this.y = y;
            this.hp = 200;
            this.power = 3;
            this.toMove = Directions.NONE;
        }

        void move(Directions direction) {
            map.get(y).replace(x, x + 1, String.valueOf(EMPTY_CHAR));
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

        void attack(Directions direction) {
            int x = this.x;
            int y = this.y;
            switch (direction) {
                case RIGHT:
                    x++;
                    break;
                case LEFT:
                    x--;
                    break;
                case DOWN:
                    y++;
                    break;
                case UP:
                    y--;
                    break;
                case NONE:
                    break;//never occur
            }
            findCreatureByLocation(x, y).loseLife(this.power);
        }

        void loseLife(int damage) {
            this.hp -= damage;
            if (this.hp <= 0) {
                this.isDead = true;
                if (isElf) {
                    numberOfElves--;
                } else {
                    numberOfGoblins--;
                }
            }
        }

        Directions moveDirection(int minDistance) {
            Directions result = Directions.NONE;
            if (distances[x][y - 1] < minDistance) {
                result = Directions.UP;
                minDistance = distances[x][y - 1];
            }
            if (distances[x - 1][y] < minDistance) {
                result = Directions.LEFT;
                minDistance = distances[x - 1][y];
            }
            if (distances[x + 1][y] < minDistance) {
                result = Directions.RIGHT;
                minDistance = distances[x + 1][y];
            }
            if (distances[x][y + 1] < minDistance) {
                result = Directions.DOWN;
            }
            return result;
        }

        Directions whereIsAdjacentEnemy() {
            char enemyChar = isElf ? GOBLIN_CHAR : ELF_CHAR;
            if (map.get(y - 1).charAt(x) == enemyChar) {
                return Directions.UP;
            }
            if (map.get(y + 1).charAt(x) == enemyChar) {
                return Directions.DOWN;
            }
            if (map.get(y).charAt(x - 1) == enemyChar) {
                return Directions.LEFT;
            }
            if (map.get(y).charAt(x + 1) == enemyChar) {
                return Directions.RIGHT;
            }
            return Directions.NONE;

        }
    }

    enum Directions {
        DOWN, LEFT, NONE, RIGHT, UP

    }
}
