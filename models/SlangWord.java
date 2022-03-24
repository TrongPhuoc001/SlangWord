package models;
public class SlangWord {
    private String word;
    private String definition;
    
    public SlangWord(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }
    
    public SlangWord(String slangWord){
        String[] value = slangWord.split(":");
        this.word = value[0];
        this.definition = value[1];
    }




    public String getWord() {
        return word;
    }
    
    public String getDefinition() {
        return definition;
    }

    public String toFileLine(){
        return word+"`"+definition;
    }
    
}