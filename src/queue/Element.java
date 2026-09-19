package queue;

class Element<T>{
    private Element<T> next;
    private T value;

    Element(Element<T> nextElement, T value) {
        this.value = value;
        this.next = nextElement;
    }
    T value() {
        return value;
    }
    Element<T> next() {
        return next;
    }
    void next(Element<T> nextElement) {
        next = nextElement;
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
