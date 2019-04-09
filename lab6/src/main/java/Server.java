import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] ar)    {
        int port = 6666;
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Waiting for a client...");

            Socket socket = ss.accept();
            System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
            System.out.println();

            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = null;
            while(true) {
                line = in.readUTF();
                final int length = Integer.parseInt(line);
                int[] array = new int[length];
                for (int i = 0; i < length; i++) {
                    array[i] = (int)(Math.random() * 1000);
                    System.out.println("" + array[i]);
                }
                System.out.println();

                final int[] array1 = array;
                final int THREAD_POOL_SIZE = 5;

                ExecutorService ExServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

                int[] sumArray = new int[THREAD_POOL_SIZE];
                for (int j = 0; j < THREAD_POOL_SIZE; j++) {
                    final int finalJ = j;
                    ExServer.execute (() -> {
                        int thread_part = finalJ;
                        for (int i = thread_part * (length / THREAD_POOL_SIZE); i < (thread_part + 1) * (length / THREAD_POOL_SIZE); i++) {
                            sumArray[thread_part] += array1[i];
                        }
                    });
                }
                ExServer.shutdown();
                ExServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

                int sum = 0;
                for (int j = 0; j < THREAD_POOL_SIZE; j++) {
                    sum += sumArray[j];
                }
                System.out.println();
                out.writeUTF("Sum array is " + String.valueOf(sum)); // отсылаем клиенту обратно ту самую строку текста.
                out.flush();
                System.out.println("Waiting for the next line...");
                System.out.println();
            }
        } catch(Exception x) { x.printStackTrace(); }
    }
}