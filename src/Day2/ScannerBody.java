package Day2;

import ImportData.ImportFromFile;

import java.util.ArrayList;

public class ScannerBody {
    private static final String FILE = "./src/Day2/packagesIDs";
    private ArrayList<String> ids = new ArrayList<>();

    public void findSolution() {
        readIDs();
        System.out.println("Scanning completed, control sum is " + findRepeatsAndReturnControlSum());
        System.out.println("Similar IDs contains letters as follow: " + findSimilarPackages());

    }

    private void readIDs() {
        ImportFromFile importFromFile = new ImportFromFile();
        ids = importFromFile.getData(FILE);
        ids.sort(String::compareTo);
    }

    private int findRepeatsAndReturnControlSum() {
        int twice = 0;
        int threeTimes = 0;
        boolean isTwice;
        boolean isThree;
        ArrayList<String> idsToCheck = new ArrayList<>(ids);

        for (String id : idsToCheck) {
            isTwice = false;
            isThree = false;
            int endOfIdName = id.length();

            while (endOfIdName > 1) {
                char checkingLetter = id.charAt(0);
                int appearanceOfCheckingLetter = 1;
                int positionOfLetterToCompere = 1;
                while (positionOfLetterToCompere < endOfIdName) {
                    if (id.charAt(positionOfLetterToCompere) == checkingLetter) {
                        appearanceOfCheckingLetter++;
                        id = id.substring(0, positionOfLetterToCompere) + id.substring(positionOfLetterToCompere + 1, endOfIdName);
                        endOfIdName--;
                    } else positionOfLetterToCompere++;
                }
                if (appearanceOfCheckingLetter == 2)
                    isTwice = true; //taka konstrukcja powoduje, że jeśli znajdę kolejną literę, która
                    // powtarza się dwa razy, nie zwiększy nam to licznika "twice";
                else if (appearanceOfCheckingLetter == 3) isThree = true;
                id = id.substring(1, endOfIdName);
                endOfIdName--;
            }
            if (isTwice) twice++;
            if (isThree) threeTimes++;
        }
        return twice * threeTimes;
    }

    private String findSimilarPackages() {
        String result = "Error! No match!";
        final int idsLength = ids.get(0).length();
        boolean match = false;
        for (String id : ids) {
            //weź każde słowo
            for (int j = ids.indexOf(id) + 1; j < ids.size(); j++) {
                //porównaj z każdym następnym
                int different = 0;
                // z założenia się nie różnią
                int differentAtCharNo = -1;
                int index = 0;
                //zacznij sprawdzanie od znaku o numerze index (czyli 0)
                while (different < 2 && index < idsLength) {
                    //dopóki różnic jest mniej niż 2 lub nie osiągneliśmy końca wyrazu sprawdzaj
                    if (id.charAt(index) != ids.get(j).charAt(index)) {
                        different++;
                        differentAtCharNo = index;
                        //jeśli się różnią zwiększ współczynnik różnicy i zaznacz na którym miejscu się różniły
                    }
                    index++;
                }
                if (different < 2) {
                    //jeśli z pętli wyszliśmy, a dalej jest mniej niż 2 różnice, to mamy ZNAJDŹKĘ
                    match = true;
                    result = id.substring(0, differentAtCharNo) + id.substring(differentAtCharNo + 1, idsLength);
                    break;
                    // przerwij dalsze poszukiwania dla i-tego słowa
                }
            }
            if (match) {
                //jeśli znaleźliśmy, przerwij poszukiwania dla pozostałych słów (pierwsze z pary)
                break;
            }
        }
        return result;
    }
}