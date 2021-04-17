package week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private static class Node<Item>{
        Node<Item> prev;
        Node<Item> next;
        Item value;

        Node(Node<Item> prev, Node<Item> next, Item value){
            this.prev = prev;
            this.next = next;
            this.value = value;
        }
    }

    private int size = 0;
    private Node<Item> first;
    private Node<Item> last;

    public Deque(){

    }

    // is the deque empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the deque
    public int size(){
        return size;
    }

    // add the item to the front
    public void addFirst(Item item){
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (size == 0){
            this.first = this.last = new Node<>(null, null, item);
        } else {
            Node<Item> node = new Node<>(null, this.first, item);
            this.first.prev = node;
            this.first = node;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item){
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (size == 0){
            this.first = this.last = new Node<>(null, null, item);
        } else {
            Node<Item> node = new Node<>(this.last ,null, item);
            this.last.next = node;
            this.last = node;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if (size == 0){
            throw new NoSuchElementException("empty deque");
        }
        Item value = this.first.value;
        if (size == 1){
            this.first = this.last = null;
        } else {
            this.first = this.first.next;
            this.first.prev = null;
        }
        size--;
        return value;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if (size == 0){
            throw new NoSuchElementException("empty deque");
        }
        Item value = this.last.value;
        if (size == 1){
            this.first = this.last = null;
        } else {
            this.last = this.last.prev;
            this.last.next = null;
        }
        size--;
        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new Iterator<>() {
            Node<Item> curr = first;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public Item next() {
                if (curr == null) {
                    throw new NoSuchElementException();
                }
                Item rs = curr.value;
                curr = curr.next;
                return rs;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> manhdt = new Deque<>();

        manhdt.addLast(1);
        manhdt.addLast(2);
        manhdt.addLast(3);

//        Iterator<Integer> iterator = manhdt.iterator();
////        System.out.println(manhdt.last.next);
//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
//            Thread.sleep(2000);
//        }

        System.out.println(manhdt.size());
        System.out.println(manhdt.removeFirst());
        System.out.println(manhdt.size());
        manhdt.addFirst(1);
        System.out.println(manhdt.size());
        System.out.println(manhdt.removeLast());
        System.out.println(manhdt.size());
    }
}