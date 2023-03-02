package opg5;

import java.util.HashMap;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

public class Opg5d2 {
    public static void main(String[] args) {

        int tableLength = 10000019; // Må være et primtall
        int numbers = 10000000;

        Random random = new Random();
        int[] tempTable = new int[tableLength];
        for (int i = 0; i < numbers; i++) {
            tempTable[i] = random.nextInt(1, tempTable.length*1000);
        }

        HashTable2 hashTable = new HashTable2(tempTable.length);

        long startTime = currentTimeMillis();

        for (int i = 0; i < numbers; i++) {
            hashTable.insert(tempTable[i]);
        }

        long endTime = currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Kollisjoner: " + hashTable.collisions);
        System.out.println("Lengde på tabell: " + (double) tableLength);
        System.out.println("Antall elementer i lista: " + (double) hashTable.filledPlaces);
        System.out.println("Lastfaktor: " + ((double) hashTable.filledPlaces / (double) tableLength));
        System.out.println("Tid brukt: " + time + " ms");



        long startTime2 = currentTimeMillis();

        HashMap<Integer, Integer> javaHashMap = new HashMap<>();

        for (int i = 0; i < numbers; i++) {
            javaHashMap.put(tempTable[i], tempTable[i]);
        }

        long endTime2 = currentTimeMillis();
        long time2 = endTime2 - startTime2;

        System.out.println("Tid brukt (Java Hashmap): " + time2 + " ms");
    }
}

class HashTable2 {
    int[] hashTable;

    int collisions = 0;
    int filledPlaces = 0;

    public HashTable2(int length) {
        this.hashTable = new int[length];
    }

    void insert(int x) {
        int pos = hash1(x);
        if (hashTable[pos] == 0) {
            hashTable[pos] = x;

            filledPlaces++;

            return;
        }
        int h2 = hash2(x) ;
        while(true) {
            pos += h2;
            if (pos >= hashTable.length) pos = pos % hashTable.length;
            if (hashTable[pos] == 0) {
                hashTable[pos] = x;

                filledPlaces++;

                return;
            } else {
                collisions++;
            }
        }
    }

    private int hash1(int x) {
        return x % hashTable.length;
    }

    private int hash2(int x) {
        return x % (hashTable.length-1) + 1;
    }
}
