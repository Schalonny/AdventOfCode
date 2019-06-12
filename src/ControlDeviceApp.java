import Day1.FrequencyBody;
import Day2.ScannerBody;
import Day3.FabricClaims;
import Day4.GuardsBehavior;
import Day5.PolymerReductor;
import Day6.CoordinatesProblem;

public class ControlDeviceApp {

    public static void main(String[] args) {
        // Day 1
        System.out.println("Time-travel divice Error. Diagnostic started...");
        FrequencyBody frequencyBody = new FrequencyBody();
        System.out.println("Sum of frequencies: " + frequencyBody.readFrequences());
        System.out.println("First repeted frequency is " + frequencyBody.repeatedFrequency());
        // Day 2
        System.out.println();
        ScannerBody scannerBody = new ScannerBody();
        scannerBody.readIDs();
        scannerBody.findSimilarPackages();
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
    }
}
