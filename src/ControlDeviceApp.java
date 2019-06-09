import Day1.FrequencyBody;
import Day2.ScannerBody;
import Day3.FabricClaims;

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
    }
}
