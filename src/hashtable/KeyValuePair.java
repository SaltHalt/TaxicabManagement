package hashtable;

class KeyValuePair<K,V>{
    private K key;
    private V value;
    KeyValuePair(K key, V value){
        this.key = key;
        this.value = value;
    }
    K key(){
        return key;
    }
    V value(){
        return value;
    }
    void value(V newValue){
        value = newValue;
    }
    public String toString(){
        return key.toString()+":"+value.toString();
    }
}
