/**
 * Defines the basics of a hash table class.
 *
 * @author Dr. Richard S. Stansbury
 *
 */


import java.io.BufferedReader;
import java.io.FileReader;

public class ProbingHashTable<T> {

    int tableSize;
    KeyedItem<T> [] table;

    /**
     * Constructor
     * @param size - size of the hash table.
     */
    public ProbingHashTable(int size)
    {
        this.table = new KeyedItem[size];
        this.tableSize = size;
    }

    /**
     * Returns the hash given a comparable key.
     *
     * @param key - key to hash
     * @return hash value or -1 if error.
     */
    public int getHash(Comparable key)
    {
        if (key instanceof Integer)
            return getIntHash(((Integer) key).intValue());
        else if (key instanceof String)
            return getStringHash((String) key);
        else {
            System.out.println("Invalid key: strings or integers only.");
            return -1;
        }
    }

    /**
     * Converts string into an integer by summing chars
     * then returns a hash given the resulting integer.
     *
     * @param str - string to hash
     * @return hash value
     */
    public int getStringHash(String str)
    {
        int val = 0;
        char [] chars = str.toCharArray();

        for (int i=0; i < chars.length; i++)
            val += chars[i];

        return getIntHash(val);
    }

    /**
     * Returns a start index into the table given a key
     * @param key - key to hash
     * @return hash value
     */
    public int getIntHash(int key)
    {
        return key % tableSize;
    }


    /**
     * Prints the items and their location in the table.
     */
    public void printTable()
    {
        System.out.println("Table Content:");
        for (int i=0; i < table.length; i++) {
            //Print if there and valid.
            if((table[i] != null) && table[i].isValid())
                System.out.println(i + "\t" + table[i]);
        }
    }

    /**
     * Adds an item to the hash table and returns its index in the table.
     * -1 implies item not added  because duplicate or full.
     * @param item - item to add
     * @return index of item added or -1 if error.
     */
    public int add(KeyedItem item) {

        int startHash = getHash(item.getKey());
        int hash = startHash;

        do {
            if ((table[hash] == null) || (!table[hash].isValid())) {
                table[hash] = item;
                return hash;
            }
            else if (item.compareTo(table[hash]) == 0) {

                //If valid, then fail.
                if (table[hash].isValid()) {
                    System.out.println("Item already in table. " + item + "--" + table[hash]);
                    return -1;
                }
                //Otherwise, continue.

            }

            System.out.println("Collision occured");

            hash = ((hash + 1) % tableSize); //increment and modulo to handle roll over
        } while (startHash != hash); //Don't want to go full circle.

        System.out.println("Table full.  Could not add item.");
        return -1;
    }


    /**
     * Deletes an item given a key and returns the item.
     * @param key - key of item being retrieved
     * @return deleted item.
     */
    public KeyedItem delete(Comparable key) {
        int startHash = getHash(key);
        int hash = startHash;

        do {
            if (table[hash] == null) {
                System.out.println("Could not find item to delete.");
                return null;
            }
            else if (key.compareTo(table[hash].getKey()) == 0) {

                //If valid, then set invalid and return the item
                if (table[hash].isValid()) {
                    table[hash].setInvalid();
                    return table[hash];
                }

                //Otherwise, keep looking just in case it was re-written later.
            }

            System.out.println("Collision occured");
            hash = ((hash + 1) % tableSize); //increment and modulo to handle roll over

        } while (startHash != hash); //Don't want to go full circle.

        System.out.println("Could not find item to delete.");
        return null;

    }


    /**
     * Retrieves an item given a key.
     * @param key - key of item being retrieved
     * @return retrieved item.
     */
    public KeyedItem get(Comparable key) {

        int startHash = getHash(key);
        int hash = startHash;

        do {
            if (table[hash] == null) {
                System.out.println("Could not find item");
                return null;
            }
            else if (key.compareTo(table[hash].getKey()) == 0) {
                //If valid, then return the item
                if (table[hash].isValid())
                    return table[hash];

                //Otherwise, keep looking just in case it was re-written later.

            }

            System.out.println("Collision occured");
            hash = ((hash + 1) % tableSize); //increment and modulo to handle roll over

        } while (startHash != hash); //Don't want to go full circle.

        System.out.println("Could not find item");
        return null;
    }

    /**
     * @paramg args
     */
    public static void main(String[] args) {
        ProbingHashTable ht = new  ProbingHashTable(31);

        String [] ids = {"1138", "1742", "1698", "1100", "0000", "1234", "9762", "1842", "9900", "2222"};
        String [] names = {"John", "Bob", "Sue", "Alice", "Jan", "Marcia", "Steven", "Negan", "Carl", "Doug"};

        for (int i=0; i < ids.length; i++) {
            ht.add(new KeyedItem<String>(ids[i], names[i]));
        }

        System.out.println();
        ht.printTable();

        System.out.println();
        System.out.println("Retrieval");
        for (int i=0; i < ids.length; i++) {
            System.out.println(ht.get(ids[i]));
        }

        System.out.println();
        System.out.println("Delete");
        ht.delete(ids[0]);
        ht.delete(ids[9]);

        System.out.println();
        ht.printTable();

        System.out.println();
        System.out.println("Retrieval Post-Delete");
        for (int i=0; i < ids.length; i++) {
            System.out.println(ht.get(ids[i]));
        }
    }

}