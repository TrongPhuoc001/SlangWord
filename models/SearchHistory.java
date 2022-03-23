package models;

import java.util.ArrayList;
import java.util.List;

public class SearchHistory {
    private String searchWord;
    private List<String> searchResult;
    private String searchTime;
    private int searchType;

    public SearchHistory(String searchWord, List<String> searchResult, String searchTime, int searchType) {
        this.searchWord = searchWord;
        this.searchResult = searchResult;
        this.searchTime = searchTime;
        this.searchType = searchType;
    }

    public SearchHistory(String fileLine){
        String[] value = fileLine.split("`");
        this.searchWord = value[0];
        this.searchResult = new ArrayList<>();
        for(String res : value[1].split(";")) {
            this.searchResult.add(res);
        }
        this.searchType = Integer.parseInt(value[2]);
        this.searchTime = value[3];
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public List<String> getSearchResult() {
        return searchResult;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public String toFileLine(){
        return searchWord + "`" + String.join(";", searchResult) + "`" + searchType + "`" + searchTime;
    }

    public String toString(){
        String res = "";
        if(this.searchType==SearchType.SEARCH_BY_WORD){
            res = "Search by word: " + this.searchWord + " at " + this.searchTime;
        }else if(this.searchType==SearchType.SEARCH_BY_DEFINITION){
            res = "Search by definition: " + this.searchWord + " at " + this.searchTime;
        }

        return res+"\n"+"Search result: " + String.join("; ", this.searchResult) + "\n";
    }

}
