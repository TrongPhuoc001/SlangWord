
import java.util.List;
import java.util.Scanner;

import service.SlangWordService;

public class ConsoleApp {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SlangWordService slangWordService = new SlangWordService("data/slang.txt");
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
                    List<String> slang = slangWordService.findSlangByWord(word);
                    if(slang == null){
                        System.out.println("Not found");
                    }
                    else{
                        System.out.println("Definition: "+slang);
                    }
                    break;
                case 2:
                    System.out.print("Enter definition: ");
                    scanner.nextLine();
                    String def = scanner.nextLine();
                    List<String> wordList = slangWordService.findSlangByDef(def);
                    if(wordList == null){
                        System.out.println("Not found");
                    }
                    else{
                        System.out.println("Word: "+ String.join("\n",wordList));
                    }
                    break;
                case 3:
                    System.out.println(slangWordService.showSearchHistory());
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    String randomSlang = slangWordService.getRandomSlang();
                    System.out.println("Random slang: "+randomSlang);
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
