package sample.io.mappedbus.sample.bytearray;
import main.io.mappedbus.MappedBusReader;

import java.util.Arrays;

public class ByteArrayReader {

	public static void main(String[] args) {
		ByteArrayReader reader = new ByteArrayReader();
		reader.run();	
	}

	public void run() {
		try {
			MappedBusReader reader = new MappedBusReader("/tmp/test-lab7", 2000000L, 10);
			reader.open();

			byte[] buffer = new byte[10];

			while (true) {
				if (reader.next()) {
					int length = reader.readBuffer(buffer, 0);
					System.out.println("Read: length = " + length + ", data= "+ Arrays.toString(buffer));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}