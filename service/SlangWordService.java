package service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import models.SearchHistory;
import models.SearchType;
import models.SlangWord;

public class SlangWordService {
    private HashMap<String, String> listSlang;
    private List<SearchHistory> searchHistory;
    private static String baseFileUrl = "data/slang.txt";
    private static String historyFileUrl = "./data/history.bin";
    private static String currentFileUrl = "./data/currentSlang.bin";
    Scanner scanner = new Scanner(System.in);

    public SlangWordService() {
        
        try {
            File currentFile = new File(currentFileUrl);
            if(currentFile.exists()){
                FileInputStream fis = new FileInputStream(currentFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.listSlang = (HashMap<String, String>) ois.readObject();
                ois.close();
            }
            else{
                loadBaseListSlang();
                saveListSlang();
            }
            if(this.listSlang.size()==0){
                loadBaseListSlang();
                saveListSlang();
            }

            File historyFile = new File(historyFileUrl);
            if(historyFile.exists()){
                FileInputStream fis = new FileInputStream(historyFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.searchHistory = (List<SearchHistory>) ois.readObject();
                fis.close();
            }
            else{
                searchHistory = new ArrayList<SearchHistory>();
            }
           
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Error reading file");
        }
        catch(ClassNotFoundException ex){
            System.out.println("Class not found");
        }
    }

    private void loadBaseListSlang(){
        this.listSlang = new HashMap<>();
        try {
            File newSlangFile = new File(baseFileUrl);
            if (newSlangFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(baseFileUrl);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line = bufferedReader.readLine();
                
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.contains("`")) {
                        continue;
                    }
                    String[] value = line.split("`");
                    if (value[0].lastIndexOf(" ") == value[0].length() - 1) {
                        value[0] = value[0].substring(0, value[0].length() - 2);
                    }
                    this.listSlang.put(value[0], value[1]);
                }
                bufferedReader.close();
            }
        } catch (Exception ex) {
            System.out.println("Error reading txt file");
            throw new RuntimeException(ex);
        }
    }



    public HashMap<String, String> getListSlang() {
        return listSlang;
    }

    public List<String> findSlangByWord(String word) {
        List<String> res = new ArrayList<>();
        for (String key : listSlang.keySet()) {
            if (key.toLowerCase().contains(word.toLowerCase())) {
                res.add(key + ":" + listSlang.get(key));
            }
        }
        SearchHistory sh = new SearchHistory(word, res, new Date().toString(), SearchType.SEARCH_BY_WORD);
        searchHistory.add(sh);
        saveSearchHistory();
        return res;
    }

    public List<String> findSlangByDef(String def) {
        List<String> result = new ArrayList<>();
        for (String key : listSlang.keySet()) {
            if (listSlang.get(key).toLowerCase().contains(def.toLowerCase())) {
                result.add(key + ":" + listSlang.get(key));
            }
        }
        SearchHistory sh = new SearchHistory(def, result, new Date().toString(), SearchType.SEARCH_BY_DEFINITION);
        searchHistory.add(sh);
        saveSearchHistory();
        return result.size() > 0 ? result : null;
    }

    public String showSearchHistory() {
        String res = "";
        for (SearchHistory sh : searchHistory) {
            res += sh.toString();
        }
        return res;
    }

    public void addSlangWord(String word, String def) {
        if (listSlang.containsKey(word)) {
            System.out.println("Already exists, do you want to update? (y/n): ");
            String choice = scanner.next();
            if (choice.equals("y")) {
                listSlang.put(word, def);
            }
        } else {
            listSlang.put(word, def);
        }
        saveListSlang();
    }

    public void editSlangWord(String word, String def) {
        if (listSlang.get(word.toUpperCase()) == null) {
            System.out.println("Slang word not found, do you want to create new one? (y/n): ");
            String choice = scanner.next();
            if (choice.equals("y")) {
                listSlang.put(word, def);
            } else {
                return;
            }
        } else {
            listSlang.put(word, def);
        }
        saveListSlang();
    }

    public void deleteSlangWord(String word) {
        if (listSlang.containsKey(word.toUpperCase())) {
            System.out.println("Are you sure you want to delete this word? (y/n): ");
            String choice = scanner.next();
            if (choice.equals("y")) {
                listSlang.remove(word);
            }
            else{
                return;
            }
            saveListSlang();
        }
        else{
            System.out.println("Slang word not found");
        }
        
    }

    private void saveListSlang(){
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(listSlang);
            FileOutputStream fileOutputStream = new FileOutputStream(currentFileUrl);
            fileOutputStream.write(byteOut.toByteArray());
            fileOutputStream.close();
        } catch (Exception ex) {
            System.out.println("Error writing file");
        }
    }

    public void reset(){
        listSlang.clear();
        loadBaseListSlang();
        saveListSlang();

    }

    public String getRandomSlang() {
        int random = (int) (Math.random() * listSlang.size());
        return listSlang.keySet().toArray()[random] + ":" + listSlang.get(listSlang.keySet().toArray()[random]);
    }

    public void quizWithSlangWord(int numberOfQuestion) {
        int correct = 0;

        for (int i = 0; i < numberOfQuestion; i++) {
            System.out.println("Question " + (i + 1));
            String randomSlang = getRandomSlang();
            SlangWord slangWord = new SlangWord(randomSlang);
            System.out.println(slangWord.getWord());
            String value = slangWord.getDefinition();
            String[] answerList = new String[4];
            for (int j = 0; j < 4; j++) {
                answerList[j] = getRandomSlang().split(":")[1];
            }
            int random = (int) (Math.random() * 4);
            answerList[random] = value;
            for (int j = 0; j < 4; j++) {
                System.out.println(j + 1 + ". " + answerList[j]);
            }
            System.out.print("Your answer: ");
            int answer;
            try {
                answer = scanner.nextInt() - 1;
            } catch (Error e) {
                System.out.println("Invalid choice");
                answer = -1;
            }
            if (answer == random) {
                System.out.println("Correct!");
                correct++;
            } else {
                System.out.println("Wrong! Answer: " + value );
            }
        }
        System.out.println("You got " + correct + "/" + numberOfQuestion);
    }

    public void quizWithSlangDefinition(int numberOfQuestion) {
        int correct = 0;
        for (int i = 0; i < numberOfQuestion; i++) {
            System.out.println("Question " + (i + 1));
            String randomSlang = getRandomSlang();
            SlangWord slangWord = new SlangWord(randomSlang);
            System.out.println(slangWord.getDefinition());
            String value = slangWord.getWord();
            String[] answerList = new String[4];
            for (int j = 0; j < 4; j++) {
                answerList[j] = getRandomSlang().split(":")[0];
            }
            int random = (int) (Math.random() * 4);
            answerList[random] = value;
            for (int j = 0; j < 4; j++) {
                System.out.println(j + 1 + ". " + answerList[j]);
            }
            System.out.print("Your answer: ");
            int answer;
            try {
                answer = scanner.nextInt() - 1;
            } catch (Error e) {
                System.out.println("Invalid choice");
                answer = -1;
            }
            if (answer == random) {
                System.out.println("Correct!");
                correct++;
            } else {
                System.out.println("Wrong! Answer: " + value);
            }
        }
        System.out.println("You got " + correct + "/" + numberOfQuestion);
    }

    private void saveSearchHistory() {
        try {
            File historyFile = new File(historyFileUrl);
            if(!historyFile.exists()){
                historyFile.createNewFile();
            }
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(searchHistory);
            FileOutputStream fileOutputStream = new FileOutputStream(historyFileUrl);
            fileOutputStream.write(byteOut.toByteArray());
            
            fileOutputStream.close();
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("Error when saving search history");
        }
    }
}
