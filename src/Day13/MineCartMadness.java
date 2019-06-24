package Day13;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class MineCartMadness implements Riddle {
    private static final String FILE = "./src/Day13/tunnels";
    private static final String DIRECTIONS = "^>v<";
    private ArrayList<StringBuilder> tunnels;
    private ArrayList<Cart> carts;


    public MineCartMadness() {
        {
            ImportFromFile importFromFile = new ImportFromFile();
            tunnels = importFromFile.getDataAsStringBuilders(FILE);
            // zmienna tunnels to kolejne linie znaków reprezentujące tunele
            this.carts = new ArrayList<>();
            for (StringBuilder row : tunnels) {
                for (int i = 0; i < row.length(); i++) {
                    // dla każdego znaku w każdym wersie
                    if (DIRECTIONS.indexOf(row.charAt(i)) >= 0) {
                        // jeśli znaleziony znak znajduje się w stałej DIRECTIONS (czyli jest kolejką)
                        carts.add(new Cart(i, tunnels.indexOf(row), row.charAt(i)));
                        // dodaj do listy kolejek nową kolejkę o zadanych współczynnikach i kierunku
                    }
                }
            }

        }
    }

    @Override
    public void findSolution() {
        int cartsOnTrack = carts.size();
        int collisionNumber =0;
        while (cartsOnTrack > 1) {                                      // dopóki jest więcej niż jedna kolejka na trasie
            carts.sort(Cart::compareTo);                                // posortuj kolejki a następnie wykonaj krotkę
            for (int cartID = 0; cartID < carts.size(); cartID++) {     // dla każdego kolejnego indeksu z tabeli kolejek
                if (carts.get(cartID).isStillOnTrack) {                 // jeśli dana kolejka jest ciągle na trasie
                    drawOnMap(cartID, true);            // zdejmij kolejkę z mapy (uzupełnij rysunek)
                    carts.get(cartID).go();                             // porusz kolejką o numerze cartID
                    if (carts.get(cartID).stepOnNewSpace(tunnels.get(carts.get(cartID).y).charAt(carts.get(cartID).x))) {
                        drawOnMap(cartID, false);       // jeśli nowe pole nie zawierało kolejki, postaw ją tam
                    } else {                                            // w przeciwnym razie podaj informacje o miejscu kolizji
                        collisionNumber++;
                        String collisionCoordinates = carts.get(cartID).x + "," + carts.get(cartID).y;
                        System.out.println(collisionNumber + ". collision will have place at location: " + collisionCoordinates);
                        foundCollisionCartAndRemoveBothFromTrack(cartID);   // zdejmij z trasy rozpatrywaną kolejkę oraz
                        cartsOnTrack -= 2;                              // tą z którą się zderzył, obniż liczbę aktywnych kolejek
                    }
                }                                                       // jeśli kolejki nie ma na trasie, omiń rozpatrywanie
            }                                                           // zakończ krotkę po sprawdzeniu wszystkich kolejek
        }
        for (Cart cart : carts) {
            if (cart.isStillOnTrack) {
                System.out.println("Cart that is still on track is now at " + cart.x + "," + cart.y);
            }
        }
    }

    private void foundCollisionCartAndRemoveBothFromTrack(int cartID) {
        carts.get(cartID).setStillOnTrack(false);                       // zdejmij z trasy rozpatrywaną kolejkę
        for (int i=0; i<carts.size(); i++) {                              // wśród wszystkich kolejek
            if (isOnTheSameSpot(carts.get(cartID), carts.get(i))) {    // wyszukaj tę, która jest na tym samym miejscu
                carts.get(i).setStillOnTrack(false);
                drawOnMap(i, true);        // pole kraksy oznacz pierwotnym symbolem
                break;                                                  // usuń odpowiednią kolejkę z trasy i zakończ pętle
            }
        }
    }

    private boolean isOnTheSameSpot(Cart cart1, Cart cart2) {
        return (cart1.y == cart2.y &&
                cart1.x == cart2.x &&
                cart2.isStillOnTrack);
    }

    private void drawOnMap(int cartID, boolean goOutRatherThanIn) {
        String toDraw;
        if (goOutRatherThanIn) {
            toDraw = Character.toString(carts.get(cartID).roadUnder);
        } else {
            toDraw = Character.toString(carts.get(cartID).direction);
        }
        int row = carts.get(cartID).y;
        int column = carts.get(cartID).x;
        tunnels.get(row).replace(column, column + 1, toDraw);
    }


    private class Cart implements Comparable<Cart> {
        int x, y;
        char direction;
        char roadUnder;
        boolean isStillOnTrack;
        CrossroadDecision crossroadDecision;

        Cart(int x, int y, char direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.isStillOnTrack = true;
            if (direction == '>' || direction == '<') {
                roadUnder = '-';
            } else roadUnder = '|';
            this.crossroadDecision = CrossroadDecision.LEFT;
        }

        void setStillOnTrack(boolean stillOnTrack) {
            isStillOnTrack = stillOnTrack;
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
