package hashtable;

import java.util.Arrays;

/**
 * @param <K> the class type of the keys
 * @param <V> the class type of the values
 */ 
public class Hashtable<K,V>{
    private final int MAX_SIZE;
    private KeyValuePair<K,V>[] hashtable;
    private int filledSlots;
    private final int NOT_FOUND = -1;
    private final KeyValuePair<K,V> EMPTY_INDEX = null;
    private final float LOAD_FACTOR = 0.7f;
    /**
     * Instantiates a hashtable
     * @param numberOfItems - the maximum number of items that are expected to be in the hashtable 
     * @param loadFactor - the load factor of the hashtable
     * When the load factor is 0.5, then the hashtable will be allocated 2*numberOfItems spaces
     * When the load factor is 1, then the hashtable will be allocated numberOfItems spaces
     * The load factor must be between 0 and 1
     */
    public Hashtable(int numberOfItems){
        MAX_SIZE = (int) (numberOfItems/LOAD_FACTOR);
        hashtable = (KeyValuePair<K,V>[]) new KeyValuePair[MAX_SIZE];
        filledSlots = 0;
    }
        
    /**Add a key-value pair to the dictionary*/
    public void add(K key, V value) {
        if(filledSlots == MAX_SIZE){
            throw new UnsupportedOperationException("Hashtable full");
        }
        int index = hash(key);
        while(hashtable[index] != EMPTY_INDEX){
            if(hashtable[index].key().equals(key)){ //checks if the key passed is already being used in hashtable
                throw new IllegalArgumentException("Key already in hashtable");
            }
            index = incr(index);
        }
        hashtable[index] = new KeyValuePair<K,V>(key, value);
        filledSlots++;
    }
    /**Returns the value associated with the given key*/
    public V item(K key) {
        int index = indexOfKey(key);
        if(index == NOT_FOUND) {
            throw new IllegalArgumentException("Key not in hashtable");
        }
        return hashtable[index].value();
    }
    /**Change the value associated with a key*/
    public void setValue(K key, V newValue){
        int index = indexOfKey(key);
        if(index == NOT_FOUND) {
            throw new IllegalArgumentException("Key not in hashtable");
        }
        hashtable[index].value(newValue);
    }
    /**Returns true if the dictionary contains the given key*/
    public boolean contains(K key) {
        return (indexOfKey(key) != NOT_FOUND);
    }
    /**Returns a string representation of the hashtable, with each entry sorted alphabetically*/
    public String toString(){
        String[] items = new String[filledSlots];
        int i = 0;
        for(KeyValuePair pair : hashtable){//create array of all key-value pairs
            if(pair != EMPTY_INDEX){
                items[i] = pair.toString();
                i++;
            }
        }
        Arrays.sort(items);
        String output = "{" + String.join(",", items) + "}";
        return output;
    }
    
    /**Returns the hash of a given key*/
    private int hash(K key){
        char[] keyCharacters = key.toString().toCharArray();
        int sum = 0;
        for (char character : keyCharacters){
            sum += (int)character;
        }
        return sum % MAX_SIZE;
    }
    /**Increments the inputted number, but sets to zero if the end of array is reached*/
    private int incr(int index){
        return (index+1)%MAX_SIZE;
    }
    
    /**Returns the index of a given key. If the key isn't found, NOT_FOUND is returned*/
    private int indexOfKey(K key){
        int index = hash(key);
        while(hashtable[index] != EMPTY_INDEX && !hashtable[index].key().equals(key)){
            index = incr(index);
        }
        return (hashtable[index] == EMPTY_INDEX)? NOT_FOUND : index;
    }    
}
