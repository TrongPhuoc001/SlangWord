
import java.util.List;
import java.util.Scanner;

import service.SlangWordService;


public class ConsoleApp {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SlangWordService slangWordService = new SlangWordService();
        int choice;
        do{
            showMenu();
            System.out.print("Your choice: ");
            try{
                choice = scanner.nextInt();
            }
            catch(Error e){
                System.out.println("Invalid choice");
                choice = -1;
            }
            switch (choice) {
                case 1:
                    System.out.print("Enter word: ");
                    String word = scanner.next();
                    long start1 = System.nanoTime();
                    List<String> slang = slangWordService.findSlangByWord(word);
                    long end1 = System.nanoTime();
                    if(slang == null){
                        System.out.println("Not found");
                    }
                    else{
                        System.out.println("Definition: "+slang);
                    }
                    System.out.println("Time: "+(end1-start1));
                    break;
                case 2:
                    System.out.print("Enter definition: ");
                    scanner.nextLine();
                    String def = scanner.nextLine();
                    long start2 = System.nanoTime();
                    List<String> wordList = slangWordService.findSlangByDef(def);
                    long end2 = System.nanoTime();
                    if(wordList == null){
                        System.out.println("Not found");
                    }
                    else{
                        System.out.println("Word: "+ String.join("\n",wordList));
                    }
                    System.out.println("Time: "+(end2-start2));
                    break;
                case 3:
                    long start3 = System.nanoTime();
                    System.out.println(slangWordService.showSearchHistory());
                    long end3 = System.nanoTime();
                    System.out.println("Time: "+(end3-start3));
                    break;
                case 4:
                    System.out.println("Enter word: ");
                    String wordNew = scanner.next();
                    scanner.nextLine();
                    System.out.println("Enter definition: ");
                    String defNew = scanner.nextLine();
                    long start4 = System.nanoTime();
                    slangWordService.addSlangWord(wordNew.toUpperCase(), defNew);
                    long end4 = System.nanoTime();
                    System.out.println("Time: "+(end4-start4));
                    break;
                case 5:
                    System.out.println("Enter word: ");
                    String wordEdit = scanner.next();
                    scanner.nextLine();
                    System.out.println("Enter definition: ");
                    String defEdit = scanner.nextLine();
                    long start5 = System.nanoTime();
                    slangWordService.editSlangWord(wordEdit.toUpperCase(), defEdit);
                    long end5 = System.nanoTime();
                    System.out.println("Time: "+(end5-start5));
                    break;
                case 6:
                    System.out.println("Enter word: ");
                    String wordDelete = scanner.next();
                    long start6 = System.nanoTime();
                    slangWordService.deleteSlangWord(wordDelete.toUpperCase());
                    long end6 = System.nanoTime();
                    System.out.println("Time: "+(end6-start6));
                    break;
                case 7:
                    long start7 = System.nanoTime();
                    slangWordService.reset();
                    long end7 = System.nanoTime();
                    System.out.println("Time: "+(end7-start7));
                    break;
                case 8:
                    long start8 = System.nanoTime();
                    String randomSlang = slangWordService.getRandomSlang();
                    long end8 = System.nanoTime();
                    System.out.println("Random slang: "+randomSlang);
                    System.out.println("Time: "+(end8-start8));
                    break;
                case 9:
                    System.out.print("Enter number of question: ");
                    int numberOfQuestion = scanner.nextInt();
                    slangWordService.quizWithSlangWord(numberOfQuestion);
                    break;
                case 10:
                    System.out.print("Enter number of question: ");
                    int numberOfQuestion1 = scanner.nextInt();
                    slangWordService.quizWithSlangDefinition(numberOfQuestion1);
                    break;
            }
        }while(choice!=0);
        scanner.close();
    }

    static void showMenu() {
        System.out.println("1. Find a slang by word");
        System.out.println("2. Find a slang by definition");
        System.out.println("3. Show search history");
        System.out.println("4. Add a new slang");
        System.out.println("5. Edit a slang");
        System.out.println("6. Delete a slang");
        System.out.println("7. Reset to base slangs");
        System.out.println("8. Choose a random slang");
        System.out.println("9. Quiz with slang words");
        System.out.println("10. Quiz with definitions");
        System.out.println("0. Quit");
    }
}
