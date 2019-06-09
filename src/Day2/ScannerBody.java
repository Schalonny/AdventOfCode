package Day2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ScannerBody {
    private static final String INPUT = "./src/Day2/packagesIDs";

    public void readIDs() {
        int twice = 0;
        int threetimes = 0;
        String line;

        try
                (FileReader fileReader = new FileReader(new File(INPUT));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                boolean isTwice = false;
                boolean isThree = false;
                int end = line.length();
                while (end > 1) {
                    char checking = line.charAt(0);
                    int i = 1;
                    int count = 1;
                    while (i < end) {
                        if (line.charAt(i) == checking) {
                            count++;
                            line = line.substring(0, i) + line.substring(i + 1, end);
                            end--;
                        } else i++;
                    }
                    if (count == 2) isTwice = true; //taka konstrukcja powoduje, że jeśli znajdę kolejną literę, która
                        // powtarza się dwa razy, nie zwiększy nam to licznika "twice";
                    else if (count == 3) isThree = true;
                    line = line.substring(1, end);
                    end--;
                }
                if (isTwice) twice++;
                if (isThree) threetimes++;
            }
        } catch (IOException e) {
            e.getMessage();
        }
        System.out.println("Control Sum of Packages is (" + twice + " * " + threetimes + ") " + twice * threetimes);
    }

    public void findSimilarPackages() {
        ArrayList<String> ids = new ArrayList<>();
        String line = null;
        try
                (FileReader fileReader = new FileReader(new File(INPUT));
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (((line = bufferedReader.readLine()) != null)) {
                ids.add(line);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        int idLength = ids.get(0).length();
        ids.sort(String::compareTo);
        boolean match = false;
        for (int i = 0; i < ids.size() - 1; i++) {
            //weź każde słowo
            for (int j = i + 1; j < ids.size(); j++) {
                //porównaj z każdym następnym
                int different = 0;
                // z założenia się nie różnią
                int differentAtCharNo = -1;
                int index = 0;
                //zacznij sprawdzanie od znaku o numerze index (czyli 0)
                while (different < 2 && index < idLength) {
                    //dopóki różnic jest mniej niż 2 lub nie osiągneliśmy końca wyrazu sprawdzaj
                    if (ids.get(i).charAt(index) != ids.get(j).charAt(index)) {
                        different++;
                        differentAtCharNo = index;
                        //jeśli się różnią zwiększ współczynnik różnicy i zaznacz na którym miejscu się różniły
                    }
                    index++;
                }
                if (different < 2) {
                    //jeśli z pętli wyszliśmy, a dalej jest mniej niż 2 różnice, to mamy ZNAJDŹKĘ
                    match = true;
                    System.out.println("ID of package is: " + ids.get(i).substring(0, differentAtCharNo) + ids.get(i).substring(differentAtCharNo + 1, idLength));
                    break;
                    // przerwij dalsze poszukiwania dla i-tego słowa
                }
            }
            if (match) {
                //jeśli znaleźliśmy, przerwij poszukiwania dla pozostałych słów (pierwsze z pary)
                break;
            }
        }

    }
}