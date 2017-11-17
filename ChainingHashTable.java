/**
 * Defines the basics of a hash table class.
 *
 * @author Dr. Richard S. Stansbury
 *
 */


import java.io.BufferedReader;
import java.io.FileReader;

public class ChainingHashTable<T> {

    int tableSize;
    HashNode<T> [] table;

    /**
     * Constructor
     * @param size - size of the hash table.
     */
    public ChainingHashTable(int size)
    {
        this.table = new HashNode[size];
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
            if(table[i] != null) {
                HashNode<T> cur = table[i];

                while (cur != null) {
                    System.out.println(i + " " + cur.info);
                    cur = cur.next;
                }
            }

        }
    }

    /**
     * Adds an item to the hash table and returns its index in the table.
     * -1 implies item not added  because duplicate or full.
     * @param item - item to add
     * @return index of item added or -1 if error.
     */
    public int add(KeyedItem<T> item) {

        int hash = getHash(item.getKey());

        if (table[hash] == null) {
            table[hash] = new HashNode<T>(item);
            return hash;
        }
        else {
            HashNode<T> cur = table[hash];
            HashNode<T> prev = null;
            while (cur != null) {
                if (item.compareTo(cur.info) == 0) {
                    System.out.println("Item already in table.");
                    return -1;
                }
                prev = cur;
                cur = cur.next;
            }

            prev.next = new HashNode<T>(item);

            return hash;
        }
    }


    /**
     * Deletes an item given a key and returns the item.
     * @param key - key of item being retrieved
     * @return deleted item.
     */
    @SuppressWarnings("unchecked")
    public KeyedItem delete(Comparable key) {
        int hash = getHash(key);

        if (table[hash] != null) {
            HashNode<T> cur = table[hash];
            HashNode<T> prev = null;
            while (cur != null) {
                if (key.compareTo(cur.info.key) == 0) {
                    if (prev == null) {
                        table[hash] = cur.next;
                    }
                    else {
                        prev.next = cur.next;
                    }
                    return cur.info;
                }
                prev = cur;
                cur = cur.next;
            }
        }
        System.out.println("Item not in table.  No value deleted or returned.");
        return null;
    }


    /**
     * Retrieves an item given a key.
     * @param key - key of item being retrieved
     * @return retrieved item.
     */
    @SuppressWarnings("unchecked")
    public KeyedItem get(Comparable key) {

        int hash = getHash(key);

        if (table[hash] != null) {
            HashNode<T> cur = table[hash];

            while (cur != null) {
                if (key.compareTo(cur.info.key) == 0) {
                    return cur.info;
                }
                cur = cur.next;
            }
        }
        System.out.println("Item not in table.  No value deleted or returned.");
        return null;
    }

    /**
     * Implements a link-list style node for chaining
     * @param <T> - type of the info stored in the keyedItem pair.
     */
    private class HashNode<T> {

        public KeyedItem<T> info;
        public HashNode<T> next;

        /**
         * Default constructor where no other nodes in the same hash table slot.
         * @param info - into to be stored in the node.
         */
        public HashNode(KeyedItem<T> info) {
            this.info = info;
            next = null;
        }

        /**
         * Constructor accepting info and reference to next node in the chain.
         * @param info - info to be stored in node
         * @param next - index of next item in chain.
         */
        public HashNode(KeyedItem<T> info, HashNode<T> next) {
            this.info = info;
            this.next = next;
        }
    }


    /**
     * @paramg args
     */
    public static void main(String[] args) {
        ChainingHashTable<String> ht = new  ChainingHashTable<>(31);
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