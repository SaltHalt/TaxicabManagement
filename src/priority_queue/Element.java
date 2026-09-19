package priority_queue;

class Element<V, P>{

    private V value;
    private P priority;
    private Element<V,P> previous;
    private Element<V,P> next;

    Element(V value, P priority, Element<V,P> previous, Element<V,P> next) {
        this.value = value;
        this.priority = priority;
        this.previous = previous;
        this.next = next;
    }

    V value() {
        return value;
    }

    void value(V newValue) {
        this.value = newValue;
    }
    P priority(){
        return priority;
    }
    Element<V,P> previous() {
        return previous;
    }

    void previous(Element<V,P> newPreviousElement) {
        previous = newPreviousElement;
    }

    Element next() {
        return next;
    }

    void next(Element<V,P> newNextElement) {
        next = newNextElement;
    }
    
    @Override
    public String toString() {
        return value.toString()+","+priority.toString();
    }
}
