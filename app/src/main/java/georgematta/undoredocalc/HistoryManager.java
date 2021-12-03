package georgematta.undoredocalc;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager{

    ArrayList<String> history;
    int currentIndex;

    public HistoryManager(){
        this.history = new ArrayList<>();
        this.currentIndex = 0;
    }

    // GETTERS
    public int getCurrentIndex(){
        return this.currentIndex;
    }
    // Returns the entire history
    public ArrayList<String> getHistory(){
        return history;
    }
    // Return the last 'window' elements in the index
    public List<String> getHistory(int window){
        int historySize = history.size();
        // Calculate the proper startIndex based on the given window
        int startIndex = historySize - window;
        if (startIndex < 0){
            startIndex = 0;
        }

        // Return a subset of the list from the startIndex to the last element
        return history.subList(startIndex, historySize);
    }

    // ARRAYLIST CONTROL
    // Returns the element at a specific index
    public String get(int index){
        if (index >= history.size()){
            return "";
        }
        return history.get(index);
    }
    // Adds an element to the end of the list
    public void add(String element){
        history.add(element);
    }
    // Adds an element to a specific index of the list
    public void add(int index, String element){
        history.add(index, element);
    }
    // Removes an element from the list given an index
    public void remove(int index){
        history.remove(index);
    }
    // Returns the total size of the history
    public int size(){
        return history.size();
    }

    // INDEX CONTROL
    // Increment or decrement in the index
    public void incrementIndex(){
        this.currentIndex++;
    }
    public void decrementIndex(){
        this.currentIndex--;
    }

    // CHECKS
    // Check if our index is at any bounds
    public boolean indexIsZero(){
        return indexIsLimit(0);
    }
    public boolean indexIsLimit(int limit){
        return this.currentIndex == limit;
    }


}
