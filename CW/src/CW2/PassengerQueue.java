package CW2;
public class PassengerQueue {
    static final int SEATING_CAPACITY = 42; // initializing seating capacity as a static final value to be used throughout the program
    private static int first;
    private static int last;
    private static int maxLength;
    private static Passenger[] queueArray = new Passenger[SEATING_CAPACITY];

    public static void add(Passenger next){
        queueArray[last] = next;
        maxLength++;
        last++;
    }
    public static Passenger remove(Passenger passenger) {
        Passenger next = queueArray[first];
        first++;
        maxLength--;
        return next;
    }
    public static Passenger[] getArray(){
        return queueArray;
    }
    public static boolean isFull()// if queue is full
    {
        return maxLength ==42;
    }
}
