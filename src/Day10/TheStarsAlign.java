package Day10;

import ImportData.ImportFromFile;
import Intarface.Riddle;

import java.util.ArrayList;

public class TheStarsAlign implements Riddle {
    private static final String FILE = "./src/Day10/initialPosition";


    @Override
    public void findSolution() {

    }



    private ArrayList<Star> initialValues(){
        ImportFromFile importFromFile = new ImportFromFile();
        ArrayList<String> rowData = importFromFile.getData(FILE);
        ArrayList<Star> data = new ArrayList<>();
        for (String star : rowData) {
            int x = Integer.valueOf(star.substring(10,15));
            int y = Integer.valueOf(star.substring(16,21));
            int vX =  Integer.valueOf(star.substring(33,35));
            int vY =  Integer.valueOf(star.substring(37,39));
            //  position=< 42772, -21149> velocity=<-4,  2>
            data.add(new Star(x,y,vX,vY));
        }
        return data;
    }

    private class Star {
        int xPosition, yPosition, xVelocity, yVelocity;

        Star(int xPosition, int yPosition, int xVelocity, int yVelocity) {
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            this.xVelocity = xVelocity;
            this.yVelocity = yVelocity;
        }
    }
}
