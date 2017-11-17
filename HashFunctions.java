/**
 * This class demonstrates a variety of hash functions.  For your particular hash table, you 
 * could use whichever you so desire.  Keep in mind that one of your goals is to ALWAYS
 * generate a hash key that will occupy the range of your hash table's size.  Thus, when you do
 * these various options, make sure that before you modulo there is a sizeable range of values that
 * may be generated given your "key".
 *
 * @author Prof. Richard S. Stansbury
 * Embry-Riddle Aeronautical University
 * Daytona Beach, FL
 *
 */


public class HashFunctions {


    /**
     * This method converts a string to an integer by summing up the
     * characters of the string.
     *
     * Alternatively, you coudl re-implement this method with a multiply, but you
     * would need to do some bounding to ensure that you do not overshoot the maximum integer
     * range.
     *
     * @param str - string that we want to convert to an integer
     * @return an integer that represents the string.
     */
    public static int stringToInt(String str)
    {
        int val = 0;
        char [] chars = str.toCharArray();

        for (int i=0; i < chars.length; i++)
            val += chars[i];

        return val;
    }


    /**
     * This is the simplest hash function out there.  Given an
     * integer reprsenting your key, you simply modulo it by your overall
     * table size.
     *
     * @param val - value that represents your key
     * @param tableSize - size of your hash table (i.e. the maximum amount of data that
     *   may be stored)
     * @return the hash into your hash table array.
     */
    public static int getModuloHash(int val, int tableSize)
    {
        return val % tableSize;
    }


    /**
     * This hash function splits your number up into clusters of digits given some
     * predefined number of digits per cluster.  These new clusters are summed together
     *
     * (e.g.  11384272 => 1138 + 4272)
     *
     * The result is moduloed to the table size so that the resulting hash table index will
     * be a valid index into the array.
     *
     * @param val - value that represents your key
     * @param digits - number of digits per cluster
     * @param tableSize - size of your table.
     * @return the hash into your hash table array
     */
    public static int getShiftFoldingHash(int val, int digits, int tableSize)
    {
        //Div gives a power of 10 number than you can use to grab yoru clusters
        int div = (int) Math.pow(10, digits);

        //Initialize the hash index to zero
        int hash = 0;

        //this goes cluster by cluser summing up the clusters
        while (val > 0) {
            hash += (val % div); //Sums the next cluster
            val = (int) (val /div);	//prepares val for next cluster
        }

        return hash % tableSize;
    }

    /**
     * See comments for shift folding.  In this method, you flip every other cluster's order
     *
     *  e.g. 11384272 = 1138 2724
     * @param val - value that represents your key
     * @param digits - number of digits per cluster
     * @param tableSize - size of your table.
     * @return the hash into your hash table array
     */
    public static int getBoundaryFoldingHash(int val, int digits, int tableSize)
    {
        //Get the divisor for removing clusters some number of "digits"
        int div = (int) Math.pow(10, digits);

        //Initialize some variables
        int hash = 0,
                tmp = 0,
                i=0;

        while (val > 0) {

            tmp = val % div; //Get cluster

            //If even, then reverse the digits
            if (i % 2 != 0) {
                tmp = reverseInt(tmp);
            }

            //Sum the tmp value into the growing hash key
            hash += tmp;
            val = (int) (val /div);//Divide out the digits just used
            i++; //Increment our counter
        }
        return hash % tableSize;
    }

    /**
     * Reverse the digits of an integer
     * e.g. 1138 =+ 8311
     * @param val - value to be reversed
     * @return - reverse of val
     */
    public static int reverseInt(int val)
    {
        int tmp = 0;
        while (val > 0) {
            tmp *= 10;
            tmp += (val % 10);
            val = val / 10;
        }
        return tmp;
    }


    /**
     * This method first squares your value and then grabs the n innermost digits.
     * This middle cluster is then moduloed by the table size.
     *
     * @param val - value representing your key
     * @param digits - the number n of digits in the middle cluster
     * @param tableSize - size of the table
     * @return - index into the hash table.
     */
    public static int getMidSquareHash(int val, int digits, int tableSize)
    {
        //Square the value - note must convert to tmp
        long tmp = ((long) val) * ((long) val);

        //Get the number of digits in the tmp variable
        int numDigits = Long.toString(tmp).length();

        //Create an appropriate divider to remove the leading and trailing
        //digits around our target cluster
        long start = (long) Math.pow(10, ((int) (numDigits - digits) / 2));

        //Gets the target cluster
        tmp = tmp / start;
        tmp = (long) tmp % (long) Math.pow(10, digits);

        //Moduloed
        return (int) (tmp % tableSize);
    }


    /**
     * Returns the digits in an integer from the leftIndex to the rightIndex.
     * This cluster is then moduloed by the table size.
     *
     * @param val - value representing our key
     * @param leftIndex - left index of the cluster (inclusive)
     * @param rightIndex - right index of the cluster (inclusive)
     * @param tableSize - size of the hash table.
     * @return index into the hash table
     */
    public static int getExtractionHash(int val, int leftIndex, int rightIndex, int tableSize)
    {
        int numDigits = Long.toString(val).length();

        //Determine the number of digits to remove from the left of the cluster
        //and calculate the divisor
        int leftDiv = (int) Math.pow(10,numDigits - leftIndex);

        //Determine the number of digits to the right of the index
        //and calculate the divisor
        int rightDiv = (int) Math.pow(10,numDigits - rightIndex-1);

        //Divide out the left digits and the right digits
        val = ((int) (val % leftDiv) / rightDiv);

        System.out.println("-"+val);

        //Moduloed
        return val % tableSize;
    }


    /**
     * This takes the value representing the key and converts it to a number of a different
     * radix.  Then, once in this new radix, if treats the new number as if it were actually base
     * 10 and then modulos it to make sure that the final number is within the bounds of the table.
     *
     * You will likely get an error if you use anything base 4 or lower for any
     * reasonably long key due to too many digits to be handled by int or long.
     *
     * @param val - value in base 10 representing the key
     * @param radix - base of the converted number
     * @param tableSize - table size
     * @return - index in base 10 for the index into the hash table.
     */
    public static int getRadixHash(int val, int radix, int tableSize)
    {
        long tmp = 0;
        try {
            //Creates a number in whatever radix you specify as an integer.  Then, the number is converted
            // to a long (too large for an int), which basically says to treat the new number as if it were
            // base 10.
            tmp = Long.parseLong(Integer.toString(val,radix));

        } catch(NumberFormatException nfe) {
            System.out.println("Radix Too Low Given Key...please try a larger radix or smaller key");
            nfe.printStackTrace();
            System.exit(0);
        }

        //Modulo to table size and convert back to int.
        return (int) (tmp % tableSize);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(HashFunctions.getExtractionHash(20071138, 4,7, 6763));
        System.out.println(HashFunctions.getExtractionHash(20078920, 4,7, 6763));
        System.out.println(HashFunctions.getExtractionHash(20079730, 4,7, 6763));

        System.out.println(Integer.toString(1704,7));
        System.out.println(Integer.toString(1802,7));
        System.out.println(Integer.toString(1711,7));
    }

}