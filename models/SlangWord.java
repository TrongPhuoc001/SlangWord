package models;
public class SlangWord {
    private String word;
    private String definition;
    private String example;
    
    public SlangWord(String word, String definition, String example) {
        this.word = word;
        this.definition = definition;
        this.example = example;
    }
    
    public String getWord() {
        return word;
    }
    
    public String getDefinition() {
        return definition;
    }
    
    public String getExample() {
        return example;
    }
}