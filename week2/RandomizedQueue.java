package week2;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int maxSize = 1;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue(){
        items = (Item[]) new Object[maxSize];
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return size;
    }

    // add the item
    public void enqueue(Item item){
        if (item == null){
            throw new IllegalArgumentException();
        }
        if (size == maxSize){
            maxSize *= 2;
            resize(maxSize);
        }
        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue(){
        if (size == 0){
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        Item value = items[index];
        items[index] = items[size-1];
        items[size-1] = null;
        size--;

        if (size > 0 && size == maxSize / 4){
            maxSize /= 2;
            resize(maxSize);
        }

        return value;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if (size == 0){
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        final Item[] iteratorItems = (Item[]) new Object[size];
        System.arraycopy(items, 0, iteratorItems, 0, size);
        StdRandom.shuffle(iteratorItems);
        return new Iterator<>() {
            int curr = 0;

            @Override
            public boolean hasNext() {
                return curr < size;
            }

            @Override
            public Item next() {
                if (curr >= size) {
                    throw new NoSuchElementException();
                }
                return iteratorItems[curr++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void resize(int n){
        Item[] newItems = (Item[]) new Object[n];
        System.arraycopy(items, 0, newItems, 0, size);
        items = newItems;
    }

    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<Integer> manhdt = new RandomizedQueue<>();
        manhdt.enqueue(1);
        manhdt.enqueue(2);
        manhdt.enqueue(3);
        manhdt.enqueue(4);
//        Iterator<Integer> iterator = manhdt.iterator();
////        System.out.println(manhdt.last.next);
//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
//            Thread.sleep(2000);
//        }

        System.out.println(manhdt.size());
        manhdt.dequeue();
        System.out.println(manhdt.size());
        System.out.println(manhdt.sample());
    }
}
