package htape.util;

import java.util.ArrayList;

public class Pool<E> {

    ArrayList<E> pool;
    private int index = 0;

    public Pool() {
        pool = new ArrayList<E>();
    }

    public void add(E el) {
        pool.add(el);
    }

    public E get() {
        return pool.get(index);
    }

    public E next(){
        index++;
        index %= pool.size();
        System.out.println("Pool item: " + get().toString());
        return get();
    }

    public E previous() {
        index--;
        index += pool.size();
        index %= pool.size();
        return get();
    }
}
