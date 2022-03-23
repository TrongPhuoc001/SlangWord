package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static String historyFileUrl = "./data/history.txt";
    public SlangWordService(String fileUrl){
        listSlang = new HashMap<String, String>();
        try {
            FileInputStream fileInputStream = new FileInputStream(fileUrl);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.contains("`")) {
                    continue;
                }
                String[] value = line.split("`");
                if (value[0].lastIndexOf(" ") == value[0].length() - 1) {
                    value[0] = value[0].substring(0, value[0].length() - 2);
                }
                listSlang.put(value[0], value[1]);
            }

            fileInputStream = new FileInputStream(historyFileUrl);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            this.searchHistory = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null){
                SearchHistory sh = new SearchHistory(line);
                searchHistory.add(sh);
            }


            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Error reading file");
        }
    }

    public HashMap<String,String> getListSlang() {
        return listSlang;
    }

    public List<String> findSlangByWord(String word){
        List<String> res = new ArrayList<>();
        for(String key: listSlang.keySet()){
            if(key.toLowerCase().contains(word.toLowerCase())){
                res.add(key + ":"+listSlang.get(key));
            }
        }
        SearchHistory sh = new SearchHistory(word, res, new Date().toString(), SearchType.SEARCH_BY_WORD);
        saveSearchHistory(sh);
        searchHistory.add(sh);
        return res;
    }

    public List<String> findSlangByDef(String def){
        List<String> result = new ArrayList<>();
        for(String key: listSlang.keySet()){
            if(listSlang.get(key).toLowerCase().contains(def.toLowerCase())){
                result.add(key+":"+listSlang.get(key));
            }
        }
        SearchHistory sh = new SearchHistory(def, result, new Date().toString(), SearchType.SEARCH_BY_DEFINITION);
        saveSearchHistory(sh);
        searchHistory.add(sh);
        return result.size()>0?result:null;
    }

    public String showSearchHistory(){
        String res = "";
        for(SearchHistory sh: searchHistory){
            res += sh.toString();
        }
        return res;
    }

    public String getRandomSlang(){
        int random = (int) (Math.random() * listSlang.size());
        return listSlang.keySet().toArray()[random] + ":" + listSlang.get(listSlang.keySet().toArray()[random]);
    }

    public void quizWithSlangWord(int numberOfQuestion){
        int correct = 0;
        Scanner scanner = new Scanner(System.in);
        for(int i = 0; i < numberOfQuestion; i++){
            System.out.println("Question "+(i+1));
            String randomSlang = getRandomSlang();
            SlangWord slangWord = new SlangWord(randomSlang);
            System.out.println(slangWord.getWord());
            String value = slangWord.getDefinition();
            String[] answerList = new String[4];
            for(int j=0;j<4;j++){
                answerList[j] = getRandomSlang().split(":")[1];
            }
            int random = (int) (Math.random() * 4); 
            answerList[random] = value;
            for(int j=0;j<4;j++){
                System.out.println(j+1+". "+answerList[j]);
            }
            System.out.print("Your answer: ");
            int answer;
            try {
                answer = scanner.nextInt()-1;
            } catch (Error e) {
                System.out.println("Invalid choice");
                answer = -1;
            }
            if(answer==random){
                System.out.println("Correct!");
                correct++;
            }
            else{
                System.out.println("Wrong! Answer: " + value + 1);
            }
        }
        System.out.println("You got "+correct+"/"+numberOfQuestion);
    }

    public void quizWithSlangDefinition(int numberOfQuestion) {
        int correct = 0;
        Scanner scanner = new Scanner(System.in);
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
                System.out.println("Wrong! Answer: " + value+1);
            }
        }
        System.out.println("You got " + correct + "/" + numberOfQuestion);
    }
    private void saveSearchHistory(SearchHistory sh){
        String fileLine = sh.toFileLine();
        File historyFile = new File(SlangWordService.historyFileUrl);
        
        try{
            historyFile.createNewFile();
            FileWriter fileWriter = new FileWriter(SlangWordService.historyFileUrl);
            fileWriter.write(fileLine);
            fileWriter.close();
        }
        catch (IOException ex){
            System.out.println("Error writing file");
        }
    }  


}   

