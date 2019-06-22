package Day13;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class MineCartMadness implements Riddle {
    private static final String FILE = "./src/Day13/tunnels";
    private static final String DIRECTIONS = "^>v<";
    private ArrayList<StringBuilder> tunnels;
    private ArrayList<Cart> carts;
    private String collisionCoordinates;


    public MineCartMadness() {
        {
            ImportFromFile importFromFile = new ImportFromFile();
            tunnels = importFromFile.getDataAsStringBuilders(FILE);
            this.carts = new ArrayList<>();
            for (StringBuilder row : tunnels) {
                for (int i = 0; i < row.length(); i++) {
                    if (DIRECTIONS.indexOf(row.charAt(i)) >= 0) {
                        carts.add(new Cart(i, tunnels.indexOf(row), row.charAt(i)));
                    }
                }
            }

        }
    }

    @Override
    public void findSolution() {
        boolean safe = true;
        while (safe) {
            for (Cart cart : carts) {
                tunnels.get(cart.y).replace(cart.x, cart.x + 1, Character.toString(cart.roadUnder));
                cart.go();
                safe  = cart.stepOnNewSpace(tunnels.get(cart.y).charAt(cart.x));
                if (!safe) {
                    collisionCoordinates = cart.x + "," + cart.y;
                    System.out.println("First collision will have place at location: " + collisionCoordinates);
                    break;
                }
                tunnels.get(cart.y).replace(cart.x, cart.x + 1, Character.toString(cart.direction));
            }
        }
    }


    private class Cart implements Comparable<Cart> {
        int x, y;
        char direction;
        char roadUnder;
        CrossroadDecision crossroadDecision;

        Cart(int x, int y, char direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            if (direction == '>' || direction == '<') {
                roadUnder = '-';
            } else roadUnder = '|';
            this.crossroadDecision = CrossroadDecision.LEFT;
        }

        void meetCrossroad() {
            switch (crossroadDecision) {
                case LEFT:
                    this.crossroadDecision = CrossroadDecision.STRAIGHT;
                    turnLeft();
                    break;
                case STRAIGHT:
                    this.crossroadDecision = CrossroadDecision.RIGHT;
                    break;
                case RIGHT:
                    this.crossroadDecision = CrossroadDecision.LEFT;
                    turnRight();
                    break;
            }

        }

        boolean stepOnNewSpace(char road) {
            this.roadUnder = road;
            if (road == '/' || road == '\\') {
                meetCurve(road);
            } else if (road == '+') {
                meetCrossroad();
            }
            return (DIRECTIONS.indexOf(road) < 0);
        }

        void meetCurve(char curve) {
            String situation = curve + "" + this.direction;
            switch (situation) {
                case "/<":
                case "\\v":
                case "\\^":
                case "/>":
                    turnLeft();
                    break;
                case "\\<":
                case "\\>":
                case "/^":
                case "/v":
                    turnRight();
                    break;
            }
        }

        @Override
        public int compareTo(Cart o) {
            return 200 * (this.y - o.y) + this.x - o.x;
        }

        void turnRight() {
            this.direction = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.direction) + 1) % 4);
        }

        void turnLeft() {
            this.direction = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.direction) + 3) % 4);
        }

        void go() {
            switch (this.direction) {
                case '^':
                    goUp();
                    break;
                case '>':
                    goRight();
                    break;
                case '<':
                    goLeft();
                    break;
                case 'v':
                    goDown();
                    break;
            }
        }

        void goUp() {
            this.y--;
        }

        void goDown() {
            this.y++;
        }

        void goLeft() {
            this.x--;
        }

        void goRight() {
            this.x++;
        }
    }

    private enum CrossroadDecision {
        LEFT, STRAIGHT, RIGHT
    }
}
