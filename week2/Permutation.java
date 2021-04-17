package week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args){
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);

        if (k == 0) {
            return;
        }

        //https://www.geeksforgeeks.org/reservoir-sampling/
        for (int i = 0; i < k; i++){
            randomizedQueue.enqueue(StdIn.readString());
        }
        for (int i = k; !StdIn.isEmpty(); i++){
            int j = StdRandom.uniform(i + 1);
            if (j < k){
                randomizedQueue.dequeue();
                randomizedQueue.enqueue(StdIn.readString());
            } else {
                StdIn.readString();
            }
        }
        while(randomizedQueue.size() != 0){
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}