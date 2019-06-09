package Day4;

import ImportData.ImportFromFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class GuardsBehavior {
    private static final String INPUT = "./src/Day4/wallData";

    private ArrayList<String> getSortedData() {
        ImportFromFile dataImport = new ImportFromFile();
        ArrayList<String> guardsList = dataImport.getData(INPUT);
        Collections.sort(guardsList);
        return guardsList;
    }

    private TreeSet<Guard> createGuardsSet(ArrayList<String> notes){
        TreeSet<Guard> timeTable = new TreeSet<>();
        for (String record : getSortedData()) {
            if (record.contains("Guard #")) {
                // getNumber
                int guardNo = Integer.parseInt(
                        record.substring(
                                record.indexOf("Guard #")+7,record.indexOf(" begins")));
                Guard guard = new Guard(guardNo);
                timeTable.
                // if this number exist - edit Guard
                    // increase sleep minutes
                    // increase shifts
                // else create new Guard(number)
                if (
            }
        }

        //[1518-08-27 00:54] wakes up
        //[1518-10-07 23:57] Guard #179 begins shift
        //[1518-10-05 00:23] falls asleep

        return timeTable;
    }

}
