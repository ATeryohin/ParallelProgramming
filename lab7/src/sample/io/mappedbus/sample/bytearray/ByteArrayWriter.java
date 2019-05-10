package sample.io.mappedbus.sample.bytearray;
import main.io.mappedbus.MappedBusWriter;

import java.util.Arrays;

public class ByteArrayWriter {

	public static void main(String[] args) {
		ByteArrayWriter writer = new ByteArrayWriter();
		writer.run(3);
	}

	public void run(int source) {
		try {
			MappedBusWriter writer = new MappedBusWriter("/tmp/test-lab7", 2000000L, 10);
			writer.open();
			
			byte[] buffer = new byte[10];

			for (int i = 0; i < 1000; i++) {
				Arrays.fill(buffer, (byte)source);
				writer.write(buffer, 0, buffer.length);
				Thread.sleep(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}