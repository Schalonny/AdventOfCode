package Day15;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

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
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(0).length(); j++) {
                distances[i][j] = INFINITY;
            }
        }
    }

    @Override
    public void findSolution() {
        int fullRoundsCompleted = -1;
        while (numberOfElves > 0 && numberOfGoblins > 0) {
            fullRoundsCompleted++;
            sortCreatures();
            for (Creature currentCreature : creatures) {
                if (!currentCreature.isDead) {
                    // jeśli nie ma już jednego z rodzajów stworków to NATYCHMIAST kończymy zabawę
                    if (numberOfElves == 0 || numberOfGoblins == 0) {
                        break;
                    }
                    // jeżeli nasz aktywny ziomek nie sąsiaduję z wrogiem i
                    // o ile ma wolne pola by się ruszyć, to znajdujemy do którego POI jest najbliżej
                    Directions attackDirection = currentCreature.whereIsAdjacentEnemyIfAny();
                    if ( attackDirection == Directions.NONE
                            && (getNeighbours(currentCreature.x, currentCreature.y, EMPTY_CHAR).size() > 0)) {
                        determineWhereToMoveAndMove(currentCreature);
                    }
                    // przy ataku, jeśli nie ma sąsiada metoda nic nie robi :)
                    currentCreature.attack(currentCreature.whereIsAdjacentEnemyIfAny());
                }
            }
            System.out.println("round: " + fullRoundsCompleted + " E:" + numberOfElves + " G:" + numberOfGoblins);
            for (StringBuilder stringBuilder : map) {
                System.out.println(stringBuilder);
            }
        }

        int sumOfHP = 0;
        for (Creature creature : creatures) {
            if (!creature.isDead) {
                sumOfHP += creature.hp;
            }
        }
        System.out.println("After " + fullRoundsCompleted + " rounds, the control number is " + sumOfHP * fullRoundsCompleted);
    }

    private void determineWhereToMoveAndMove(Creature active) {
        // znajdź wszystkich wrogów (a dokładnie miejsca dookoła tych wrogów - nasze Points Of Intrest)
        ArrayList<Coordinates> targetCoordinates = new ArrayList<>();
        creatures.stream()
                .filter(c -> c.isElf != active.isElf && !c.isDead) //find all living enemies
                .collect(Collectors.toList())
                .forEach(c -> targetCoordinates.addAll(getNeighbours(c.x, c.y, EMPTY_CHAR)));
        Collections.sort(targetCoordinates);
        // określamy jako odległość od aktywnego stworka do najbliższego POI nieskończoność
        int minDistance = INFINITY;
        // Dla każdego POI musimy wytoczyć trasę do aktywnego stworka, w tym celu wypełniamy tablicę podobną do tunelów
        // wartościami jak daleko to pole jest od pola startowego (od POI)
        for (Coordinates target : targetCoordinates) {
            minDistance = calculateDistanceTo(target.x, target.y, active, minDistance);
        }
        // jeśli znaleźliśmy chociaż jedną ścieżkę, to znaczy minDistance uległ zmniejszeniu, to IDZIEMY
        if (minDistance < INFINITY) {
            active.move(active.moveDirection(minDistance));
        }
        clearDistances();
    }

    private int calculateDistanceTo(int x, int y, Creature attacker, int minDistance) {
        // zaczynamy ruch w w POI, więc odległość jest równa 0
        int currentDistance = 0;
        // wypełniamy mapę odległości wartościami, zaczynając od 0 w (x,y)
        fillMapOfDistances(x, y, currentDistance);
        for (Coordinates neighbour : getNeighbours(attacker.x, attacker.y, EMPTY_CHAR)) {
            if (distances[neighbour.x][neighbour.y] < minDistance) {
                minDistance = distances[neighbour.x][neighbour.y];
                // thanks to reading order of getNeighbour first will be determine distance to first in reading order
                // and than others (so if distance is equal, we take first (so in reading order)
                attacker.toMove = getDirection(attacker.x, attacker.y, neighbour.x, neighbour.y);
            }
        }
        return minDistance;
    }

    private void fillMapOfDistances(int x, int y, int currentDistance) {
        // by wypełnić mapę odległośći najpierw sprawdzamy czy dane miejsce jest puste
        // i czy aktualny dystans jest mniejszy od tego, który znajduje się w tym polu
        // (w ten sposób miejsce docelowe nie będzie mogło być wypełnione żądaną wielkością!) - ale tego nie potrzeba
        if (map.get(y).charAt(x) == EMPTY_CHAR && currentDistance < distances[x][y]) {
            //won't be out of boundary, becouse there is # all around
            distances[x][y] = currentDistance;
            currentDistance++;
            fillMapOfDistances(x - 1, y, currentDistance);
            fillMapOfDistances(x, y - 1, currentDistance);
            fillMapOfDistances(x + 1, y, currentDistance);
            fillMapOfDistances(x, y + 1, currentDistance);
        }
    }

    private Creature findCreatureByLocation(int x, int y) {
        for (Creature creature : creatures) {
            if (creature.x == x && creature.y == y && !creature.isDead) {
                return creature;
            }
        }
        return null; //never occur
    }

    private ArrayList<Coordinates> getNeighbours(int x, int y, char what) {
        ArrayList<Coordinates> result = new ArrayList<>();
        Coordinates pair = new Coordinates(x, y - 1);
        if (map.get(pair.y).charAt(pair.x) == what) {
            result.add(pair);
        }
        Coordinates pair2 = new Coordinates(x - 1, y);
        if (map.get(pair2.y).charAt(pair2.x) == what) {
            result.add(pair2);
        }
        Coordinates pair3 = new Coordinates(x + 1, y);
        if (map.get(pair3.y).charAt(pair3.x) == what) {
            result.add(pair3);
        }
        Coordinates pair4 = new Coordinates(x, y + 1);
        if (map.get(pair4.y).charAt(pair4.x) == what) {
            result.add(pair4);
        }
        return result; // return list of spaces adjacent to (x,y) contains "what"
    }


    private void sortCreatures() {
        creatures.sort(Comparator.comparingInt(c -> c.y * INFINITY + c.x));
    }


    private Directions getDirection(int fromX, int fromY, int toX, int toY) {
        // metoda przyjmuje wspolrzędne dwóch sąsiadujących pól i zwraca kierunek oraz zwrot OD -> DO
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
            fillSpaceWith(EMPTY_CHAR);
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
            fillSpaceWith(creatureSymbol());
        }

        void fillSpaceWith(char thisSymbol) {
            map.get(this.y).replace(this.x, this.x + 1, String.valueOf(thisSymbol));
        }

        char creatureSymbol() {
            return this.isElf ? ELF_CHAR : GOBLIN_CHAR;
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
                    return;
            }
            Objects.requireNonNull(findCreatureByLocation(x, y)).loseLife(this.power);
        }

        private void loseLife(int damage) {
            this.hp -= damage;
            if (this.hp <= 0) {
                this.hp = 0;
                this.isDead = true;
                fillSpaceWith(EMPTY_CHAR);
                reduceNumberOfCorrespondingCreatures(this.isElf);
            }
        }

        void reduceNumberOfCorrespondingCreatures(boolean isElf) {
            if (isElf) {
                numberOfElves--;
            } else {
                numberOfGoblins--;
            }
        }

        Directions moveDirection(int minDistance) {
            // metoda rozstrzygająca w którym ruchu poruszymy stwora
            // badamy w którym z sąsiedznich pól, tablica odległości ma znalezioną najniższą wartość
            // i w tym kierunku poruszymy stworem
            if (distances[x][y - 1] == minDistance) {
                return Directions.UP;
            }
            if (distances[x - 1][y] == minDistance) {
                return Directions.LEFT;
            }
            if (distances[x + 1][y] == minDistance) {
                return Directions.RIGHT;
            }
            if (distances[x][y + 1] == minDistance) {
                return Directions.DOWN;
            }
            return Directions.NONE; //never occur
        }

        Directions whereIsAdjacentEnemyIfAny() {
            // definiujemy znak wroga
            char enemyChar = this.isElf ? GOBLIN_CHAR : ELF_CHAR;
            if (map.get(this.y - 1).charAt(this.x) == enemyChar) {
                return Directions.UP;
            }
            if (map.get(this.y).charAt(this.x - 1) == enemyChar) {
                return Directions.LEFT;
            }
            if (map.get(this.y).charAt(this.x + 1) == enemyChar) {
                return Directions.RIGHT;
            }
            if (map.get(this.y + 1).charAt(this.x) == enemyChar) {
                return Directions.DOWN;
            }
            return Directions.NONE;

        }
    }

    enum Directions {
        DOWN, LEFT, NONE, RIGHT, UP
    }

    private static class Coordinates implements Comparable<Coordinates> {
        int x;
        int y;

        private Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Coordinates o) {
            return 100 * (this.y - o.y) + (this.x - o.x);
        }
        // Ta klasa ma za zadanie porównywać pozycję stworzeń by rozstrzygać remisy, zgodnie z treścią
        // zadania, jeśli mogę wybrać klika opcji, powinniśmy wybierać poczynając od tego który jest wyżej,
        // a jeśli są na tym samym poziomie to tego, który jest z lewej.
    }
}