package Day13;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

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
/*        boolean safe = true;
        // załóż, że trasa jest bezpieczna
        while (safe) {
            // dopóki nie zdaży się żaden wypadek
            carts.sort(Cart::compareTo);
            for (Cart cart : carts) {
                // dla każdej kolejki
                tunnels.get(cart.y).replace(cart.x, cart.x + 1, Character.toString(cart.roadUnder));
                // podmień symbol kolejki na symbol tunelu, który się pod nim znajduje
                cart.go();
                // pojedź kolejką, tj. zmień jej pozycje oraz orientacje jeśli napotka na zakręt
                safe = cart.stepOnNewSpace(tunnels.get(cart.y).charAt(cart.x));
                // sprawdz zachowanie/stan kolejki na nowym polu - jeśli była już tam kolejka metoda zwraca FALSE
                // stąd zmienna safe przyjmuje teraz odpowiednią wartość
                if (!safe) {
                    // jeśli było niebezpieczeństwo wydrukuj informacje o tym
                    collisionCoordinates = cart.x + "," + cart.y;
                    System.out.println("First collision will have place at location: " + collisionCoordinates);
                    break;
                }
                tunnels.get(cart.y).replace(cart.x, cart.x + 1, Character.toString(cart.direction));
                // narysuj kolejkę na nowym miejscu
            }
        }*/
        while (carts.size() > 1) {
            carts.sort(Cart::compareTo);
            // dopóki jest więcej niż jedna kolejka
            for (int cartID = 0; cartID<carts.size();cartID++) {
                // wykonaj krotkę
                    // dla każdej kolejki
                    tunnels.get(carts.get(cartID).y).replace(carts.get(cartID).x, carts.get(cartID).x + 1, Character.toString(carts.get(cartID).roadUnder));
                    // podmień symbol kolejki na symbol tunelu, który się pod nim znajduje
                    carts.get(cartID).go();
                    // pojedź kolejką, tj. zmień jej pozycje oraz orientacje jeśli napotka na zakręt
                    // sprawdz zachowanie/stan kolejki na nowym polu - jeśli była już tam kolejka metoda zwraca FALSE
                    if (carts.get(cartID).stepOnNewSpace(tunnels.get(carts.get(cartID).y).charAt(carts.get(cartID).x))) {
                         tunnels.get(carts.get(cartID).y).replace(carts.get(cartID).x, carts.get(cartID).x + 1, Character.toString(carts.get(cartID).direction));
                    } else {
                        // jeśli było niebezpieczeństwo wydrukuj informacje o tym
                        collisionCoordinates = carts.get(cartID).x + "," + carts.get(cartID).y;
                        System.out.println("Next collision will have place at location: " + collisionCoordinates);
                        tunnels.get(carts.get(cartID).y).replace(carts.get(cartID).x, carts.get(cartID).x + 1, Character.toString(carts.get(cartID).roadUnder));
                        boolean foundCollisionCart = false;
                        int collisionCartsID = -1;
                        while (!foundCollisionCart) {
                            collisionCartsID++;
                            if (carts.get(collisionCartsID).y==carts.get(cartID).y && carts.get(collisionCartsID).x == carts.get(cartID).x && collisionCartsID!=cartID) {
                                foundCollisionCart = true;
                                carts.remove(Math.max(collisionCartsID,cartID));
                                carts.remove(Math.min(collisionCartsID,cartID));
                                System.out.println(carts.size());
                                if (collisionCartsID<cartID) { cartID--;}
                                cartID--;
                            }
                        }
                    }
                    // narysuj kolejkę na nowym miejscu
                }
            }
            // jeśli stepOnNewSpace zwraca FALSE (potencjalny wypadek) sprawdź czy tamta kolejka nie jest do usunięcia
            // oznacz kolejkę jako "do usnięcia"
            // oznacz kolejkę na którą nastąpiliśmy jako "do usunięcia"

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
