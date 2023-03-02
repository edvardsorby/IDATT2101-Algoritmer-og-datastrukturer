package opg5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Opg5d1 {
    public static void main(String[] args) {

        ArrayList<String> names = readFromFile();

        HashTable hashTable = new HashTable(names.size());

        for (String name : names) {
            hashTable.insert(name);
        }

        hashTable.print();
        System.out.println("Antall navn: " + names.size());
        System.out.println("Antall kollisjoner: " + hashTable.getCollisions());

        double people = names.size();
        double tableSize = hashTable.getHashTableSize();

        System.out.println("Lastfaktor: " + people/tableSize);

        double collisions = hashTable.getCollisions();
        System.out.printf("Antall kollisjoner pr. person: %.2f %n", (collisions/people));

        System.out.println(hashTable.findName("Edvard Sørby"));
    }

    private static ArrayList<String> readFromFile() {
        ArrayList<String> names = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/opg5/navn.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
}
class HashTable {
    private int collisions;
    private LinkedList<String>[] hashTable;

    public HashTable(int lenght) {
        this.hashTable = new LinkedList[lenght];
    }

    void insert(String name) {
        if (name.length() == 0) return;
        int hashValue = hash(name);

        if (hashTable[hashValue] == null) {
            hashTable[hashValue] = new LinkedList<>();
        } else {
            collisions++;
            System.out.println("Kollisjon!");
            System.out.println("Navn som kolliderer: " + name + " og " + hashTable[hashValue]);
        }
        hashTable[hashValue].addLast(name);
    }

    private int hash(String text) {
        int hashValue = 0;
        for (int i = 0; i < text.length(); i++) {
            int number = text.charAt(i);
            hashValue += (number*(i+1));
        }
        return hashValue % hashTable.length;
    }

    String findName(String name) {
        int hashValue = hash(name);

        LinkedList<String> position = hashTable[hashValue];
        if (position != null) {
            for (int i = 0; i < position.size(); i++) {
                if (position.get(i).equals(name)) {
                    return (name + " finnes i tabellen på posisjon " + hashValue);
                }
            }
        }
        return name + " finnes ikke i tabellen";
    }

    void print() {
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null) {
                for (int j = 0; j < hashTable[i].size(); j++) {
                    System.out.println(hashTable[i].get(j));
                }
            }
        }
    }

    public int getCollisions() {
        return collisions;
    }

    public int getHashTableSize() {
        return hashTable.length;
    }
}