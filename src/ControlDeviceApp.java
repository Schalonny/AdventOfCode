import Day10.TheStarsAlign;
import Day11.ChronalCharge;
import Day12.SubterraneanSustainability;
import Day13.MineCartMadness;
import Day14.ChocolateCharts;
import Day15.BeverageBandits;
import Day16.ChronalClassification;
import Day17.ReservoirResearch;
import Day8.NavigationSystem;
import Day9.MarbleMania;
import ImportData.ImportFromFile;

public class ControlDeviceApp {
    private static final String INTRO = "./src/intro";

    public static void main(String[] args) {
        /*ImportFromFile importFromFile = new ImportFromFile();
        importFromFile.getData(INTRO).forEach(System.out::println);
        // Day 1
        FrequencyBody frequencyBody = new FrequencyBody();
        frequencyBody.findSolution();
        // Day 2
        ScannerBody scannerBody = new ScannerBody();
        scannerBody.findSolution();
        // Day 3
        System.out.println();
        FabricClaims fabricClaims = new FabricClaims();
        System.out.println("That many square inches has multiple claims: " + fabricClaims.multipleClaims());
        System.out.println("Only claim that isn't overlap is claim of Elf no: " + fabricClaims.untouchedClaim());
        // Day 4
        System.out.println();
        GuardsBehavior guards = new GuardsBehavior();
        System.out.println("Control sum for which guard sleep most and the best minute to slip in is: " + guards.findControlSum());
        System.out.println("Control sum for which guard is most frequently asleep on the same minute: " + guards.findSleepMinute());
        // Day 5
        System.out.println();
        PolymerReductor polymerReductor = new PolymerReductor();
        System.out.println("Length of the Polymer after reductions is: " + polymerReductor.getPolymerLength(polymerReductor.getPolymer()));
        System.out.println("Shortest Polymer after removing one letter and redact will be at length: " + polymerReductor.removeProblematicUnit());
        // Day 6
        CoordinatesProblem coordinatesProblem = new CoordinatesProblem();
        coordinatesProblem.findSolution();
        //TODO add menu and change code to looks like from day 6.

        // Day 7
        SleighInstructions sleighInstructions = new SleighInstructions();
        sleighInstructions.findSolution();



       // Day 8
        NavigationSystem navigationSystem = new NavigationSystem();
        navigationSystem.findSolution();


        // Day 9
        MarbleMania marbleMania = new MarbleMania();
        marbleMania.findSolution();
         */

        // Day 10
        // new TheStarsAlign().findSolution();

        // Day 11
        // new ChronalCharge().findSolution();

        // Day 12
        // new SubterraneanSustainability().findSolution();

        // Day 13
        // new MineCartMadness().findSolution();

        // Day 14
        // new ChocolateCharts().findSolution();

        // Day 15
        //new BeverageBandits().findSolution();

        // Day 16
        // new ChronalClassification().findSolution();

        // Day 17
        new ReservoirResearch().findSolution();
    }
}
