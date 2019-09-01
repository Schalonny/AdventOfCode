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
    private int numberOfElves;
    private int numberOfGoblins;
    private int maxElves;

    public BeverageBandits() {
        importData();
    }

    private void importData(){

        ImportFromFile importFromFile = new ImportFromFile();
        map = importFromFile.getDataAsStringBuilders(FILE);
        creatures = new ArrayList<>();
        numberOfElves = 0;
        numberOfGoblins = 0;
        for (StringBuilder line : map) {
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == ELF_CHAR) {
                    creatures.add(new Elf(x, map.indexOf(line)));
                    numberOfElves++;
                } else if (line.charAt(x) == GOBLIN_CHAR) {
                    creatures.add(new Goblin(x, map.indexOf(line)));
                    numberOfGoblins++;
                }
            }
        }
        distances = new int[map.get(0).length()][map.size()];
        clearDistances();
        maxElves = numberOfElves;
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
        int elvesPower = 3;
        fightWithElvesPowerOf(elvesPower);
        do {
            System.out.print("Elves power is now "+ ++elvesPower + " result: ");
            importData();
            fightWithElvesPowerOf(elvesPower);
        } while (maxElves!=numberOfElves);
    }

    private void fightWithElvesPowerOf(int elvesPower){
        setElvesPower(elvesPower);
        int fullRoundsCompleted = -1;
        while (numberOfElves > 0 && numberOfGoblins > 0) {
            fullRoundsCompleted++;
            sortCreatures();
            for (Creature currentCreature : creatures) {
                // jeśli nie ma już jednego z rodzajów stworków to NATYCHMIAST kończymy zabawę
                if (numberOfElves == 0 || numberOfGoblins == 0) {
                    break;
                }
                if (!currentCreature.isDead) {
                    // Tworzymy listę sąsiądujących wrogów (posortowana zgodnie z zadaniem) (może być pusta)
                    ArrayList<Coordinates> enemiesToAttack = getNeighbours(currentCreature.x,currentCreature.y,currentCreature.enemySymbol());
                    // Jeśli nie ma w sąsiedztwie wroga - ruch oraz rozejrzenie się za nowymi wrogami
                    if (enemiesToAttack.size()==0){
                        determineWhereToMoveAndMove(currentCreature);
                        enemiesToAttack = getNeighbours(currentCreature.x,currentCreature.y,currentCreature.enemySymbol());
                    }
                    // Jeśli mamy w sąsiedztwie wroga - atak!
                    if (enemiesToAttack.size()>0) {
                        currentCreature.attack(enemiesToAttack.get(idOfEnemyWithLowestHP(enemiesToAttack)));
                    }
                }
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

    private void setElvesPower(int elvesPower) {
        for (Creature creature : creatures) {
            if (creature instanceof Elf) {
                creature.power = elvesPower;
            }
        }
    }

    private int idOfEnemyWithLowestHP(ArrayList<Coordinates> enemiesToAttack) {
        int result = 0;
        int lowestHP=INFINITY;
        for (Coordinates enemy : enemiesToAttack) {
            int thisHP = findCreatureByLocation(enemy.getX(),enemy.getY()).hp;
            if (thisHP<lowestHP){
                lowestHP = thisHP;
                result = enemiesToAttack.indexOf(enemy);
            }
        }
        return result;
    }

    private void determineWhereToMoveAndMove(Creature active) {
        // znajdź wszystkich wrogów (a dokładnie miejsca dookoła tych wrogów - nasze Points Of Intrest)
        ArrayList<Coordinates> targetCoordinates = new ArrayList<>();
        creatures.stream()
                .filter(c -> (active.enemySymbol()==c.creatureSymbol()) && !c.isDead) //find all living enemies
                .collect(Collectors.toList())
                .forEach(c -> targetCoordinates.addAll(getNeighbours(c.x, c.y, EMPTY_CHAR)));
        Collections.sort(targetCoordinates);

        if (targetCoordinates.size()!=0){

            // zmierz odległości od aktywnego stwora w dowolne miejsce (reachable) na mapie
            active.fillSpaceWith(EMPTY_CHAR);
            fillMapOfDistances(active.x,active.y,0);
            active.fillSpaceWith(active.creatureSymbol());
            // znajdź wśród potencjalnych celów najbliższy
            Coordinates pointOfInterest = findNearestPointOfInterest(targetCoordinates);
            // zmierz odległości ze znalezionego POI do głównego stworka
            fillMapOfDistances(pointOfInterest.getX(),pointOfInterest.getY(),0);

            // ruch - jeśli wszystkie pola dookoła naszego stworka są INFINITY to nigdzie nie pójdzie - kierunek NONE
            active.move(active.moveDirection());
            clearDistances();
        }
    }

    private Coordinates findNearestPointOfInterest(ArrayList<Coordinates> targetCoordinates) {
        Coordinates nearestSoFar = new Coordinates(0, 0);
        int minDistance = INFINITY;
        for (Coordinates targetCoordinate : targetCoordinates) {
            if (distances[targetCoordinate.getX()][targetCoordinate.getY()] < minDistance) {
                minDistance = distances[targetCoordinate.getX()][targetCoordinate.getY()];
                nearestSoFar=targetCoordinate;
            }
        }
        clearDistances();
        return nearestSoFar;
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
        if (map.get(pair.getY()).charAt(pair.getX()) == what) {
            result.add(pair);
        }
        Coordinates pair2 = new Coordinates(x - 1, y);
        if (map.get(pair2.getY()).charAt(pair2.getX()) == what) {
            result.add(pair2);
        }
        Coordinates pair3 = new Coordinates(x + 1, y);
        if (map.get(pair3.getY()).charAt(pair3.getX()) == what) {
            result.add(pair3);
        }
        Coordinates pair4 = new Coordinates(x, y + 1);
        if (map.get(pair4.getY()).charAt(pair4.getX()) == what) {
            result.add(pair4);
        }
        return result; // return list of spaces adjacent to (x,y) contains "what"
    }


    private void sortCreatures() {
        creatures.sort(Comparator.comparingInt(c -> c.y * INFINITY + c.x));
    }

    class Elf extends Creature{
        Elf(int xPosition, int yPosition) {
            super(xPosition, yPosition);
        }

        @Override
        char creatureSymbol() {
            return 'E';
        }

        @Override
        void reduceNumberOfCorrespondingCreatures() {
            numberOfElves--;
        }

        @Override
        char enemySymbol() {
            return 'G';
        }
    }

    class Goblin extends Creature{
        Goblin(int xPosition, int yPosition) {
            super(xPosition, yPosition);
        }

        @Override
        char creatureSymbol() {
            return 'G';
        }

        @Override
        void reduceNumberOfCorrespondingCreatures() {
            numberOfGoblins--;
        }

        @Override
        char enemySymbol() {
            return 'E';
        }
    }

    abstract class Creature {
        boolean isDead;
        int x;
        int y;
        int hp;
        int power;
        Directions toMove;

        Creature(int x, int y) {
            this.isDead = false;
            this.x = x;
            this.y = y;
            this.hp = 200;
            this.power = 3;
            this.toMove = Directions.NONE;
        }

        abstract char creatureSymbol();
        abstract void reduceNumberOfCorrespondingCreatures();
        abstract char enemySymbol();

        void move(Directions direction) {

            if (direction == Directions.NONE) {
                return;
            }

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


        void attack(Coordinates victim) {
            Objects.requireNonNull(findCreatureByLocation(victim.getX(), victim.getY())).loseLife(this.power);
        }

        private void loseLife(int damage) {
            this.hp -= damage;
            if (this.hp <= 0) {
                this.hp = 0;
                this.isDead = true;
                fillSpaceWith(EMPTY_CHAR);
                reduceNumberOfCorrespondingCreatures();
            }
        }


        Directions moveDirection() {
            // metoda rozstrzygająca w którym ruchu poruszymy stwora
            // badamy w którym z sąsiedznich pól, tablica odległości ma znalezioną najniższą wartość
            // i w tym kierunku poruszymy stworem
            int minDistance = INFINITY;
            Directions result = Directions.NONE;

            if (distances[x][y-1] < minDistance) {
                minDistance = distances[x][y-1];
                result = Directions.UP;
            }
            if (distances[x - 1][y] < minDistance) {
                minDistance = distances[x - 1][y];
                result = Directions.LEFT;
            }
            if (distances[x + 1][y] < minDistance) {
                minDistance = distances[x + 1][y];
                result = Directions.RIGHT;
            }
            if (distances[x][y + 1] < minDistance) {
                result = Directions.DOWN;
            }
            return result;
        }

    }
}