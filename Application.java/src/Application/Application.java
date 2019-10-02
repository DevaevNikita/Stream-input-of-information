package Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Application {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Connection connection = Connection.getConnection();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ExecutorService es = Executors.newFixedThreadPool(3);
        List<Future<Boolean>> future = new ArrayList<>();

        System.out.println("Введите три строки: ");

        for(int i = 0; i < 3; i++){
            future.add(es.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    synchronized (connection) {
                        connection.produce();
                    }
                    return true;
                }
            }));
        }

        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
        connection.consumer();

    }
}

class Connection{
    private static Connection connection = new Connection();
    private Semaphore semaphore = new Semaphore(1);

    private MyString myString = MyString.CreateMyString();

    private Connection(){

    }

    public static Connection getConnection() {
        return connection;
    }

    public void produce(){
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        myString.Append(str);
    }

    public void consumer(){
        System.out.println(myString.getMystring());
    }

}

class MyString {
    private StringBuilder mystring;

    private MyString(){
        this.mystring = new StringBuilder();
    }

    private MyString(String str){
        this.mystring = new StringBuilder(str);
    }

    public static MyString CreateMyString(){
        return new MyString();
    }

    public static MyString CreateMyString(String str){
        return new MyString(str);
    }

    public StringBuilder getMystring() {
        return mystring;
    }

    public void Append(String str){
        this.mystring.append(str + " ");
    }

    public void Insert(int index, char c){
        this.mystring.insert(index, c);
    }
}
