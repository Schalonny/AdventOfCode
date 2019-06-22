package Day13;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class MineCartMadness implements Riddle {
    private static final String FILE = "./src/Day13/tunels";
    private static final String DIRECTIONS = "^>v<";
    private ArrayList<Cart> carts;


    public MineCartMadness() {{
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> data = importFromFile.getData(FILE);
        // w zmiennej "data" zapisz wszystkie wiersze zasad z pliku wejściowego
        this.carts = new ArrayList<>();
        for (String datum : data) {
            for (int i = 0; i < datum.length(); i++) {
                if (datum.charAt(i)==('^'|'v'|'>'|'<')) {
                    //carts.add()
                }

            }
        }

    }
    }

    @Override
    public void findSolution() {

    }


    private class Cart implements Comparable<Cart>{
        int x, y;
        char direction;
        CrossroadDecision crossroadDecision;

        public Cart(int x, int y, char direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.crossroadDecision = CrossroadDecision.LEFT;
        }

        void meetCrossroad(){
            switch (crossroadDecision){
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

        void meetCurve(char curve){
            String situation = curve + "" + this.direction;
            switch (situation){
                case "/<" :
                case "\\v":
                    turnLeft();
                    break;
                case "\\<":
                case "/^":
                    turnRight();
                    break;
            }
        }

        @Override
        public int compareTo(Cart o) {
            return 200*(o.y - this.y) + o.x - this.x;
        }

        void turnRight(){
            this.direction = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.direction)+1) % 4);
        }

        void turnLeft(){
            this.direction = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.direction)+3) % 4);
        }

        void go(){
            switch (this.direction){
                case '^': goUp(); break;
                case '>': goRight(); break;
                case '<': goLeft(); break;
                case 'v': goDown(); break;
            }
        }

        void goUp(){
            this.y--;
        }

        void goDown(){
            this.y++;
        }

        void goLeft(){
            this.x--;
        }

        void goRight(){
            this.x++;
        }
    }

    private enum CrossroadDecision {
        LEFT, STRAIGHT, RIGHT
    }
}
