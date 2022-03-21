package service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlangWordService {
    private HashMap<String, String> listSlang;
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
                String[] value1 = value[1].split("\\|");
                listSlang.put(value[0], value[1]);
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

    public String findSlangByWord(String word){
        return listSlang.get(word.toUpperCase());
    }

    public List<String> findSlangByDef(String def){
        List<String> result = new ArrayList<>();
        for(String key: listSlang.keySet()){
            if(listSlang.get(key).toLowerCase().contains(def.toLowerCase())){
                result.add(key);
            }
        }
        return result.size()>0?result:null;
    }
    
}
