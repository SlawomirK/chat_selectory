package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

//Klasa pomocnicza, w wielu zadaniach powtarza się koniecznosc czytania i pisania za posrednictwem channel więc
//dla ulatwienia ...
public class Komunikuj {
	private static String kodowanie = null;
	private final static int SIZE = 124;
	private static Charset charset = null;
	// Bufor bajtowy - do niego są wczytywane dane z kanału
	// private ByteBuffer bbuf = ByteBuffer.allocate(SIZE);
	// Tu będzie zlecenie do pezetworzenia

	private String CHARSET = "ISO-8859-2";
	private static String hitoria;

	public Komunikuj() {

		charset = Charset.forName(CHARSET);

	}

	private SocketChannel schannel;

	public void wyslij(String gadu, SocketChannel sc) {
		// TODO Auto-generated method stub
		schannel = sc;
		if (!schannel.isOpen()) {
			try {
				schannel = SocketChannel.open(new InetSocketAddress("localhost", 49998));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ByteBuffer bbuf = ByteBuffer.allocate(SIZE);
		bbuf.clear();
		gadu += "\n";
		bbuf = charset.encode(gadu);
		while (bbuf.hasRemaining()) {
			try {			
				schannel.write(bbuf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public String czytaj(SocketChannel sc) throws IOException {
		ByteBuffer bbuf = ByteBuffer.allocate(SIZE);
		StringBuffer stringBuf = new StringBuffer();
		stringBuf.setLength(0);
		bbuf.clear();
		if (!sc.isOpen()) {
			System.out.println("polaczenie zamkniete");
			schannel = SocketChannel.open();
			System.out.println("Polaczenie otwarte");
		}
		readLoop: while (true) {
			if (sc.read(bbuf) > 0) {
				bbuf.flip();
				CharBuffer cbuf = charset.decode(bbuf);
				while (cbuf.hasRemaining()) {
					char c = cbuf.get();
					if (c == '\r' || c == '\n')
						break readLoop;
					stringBuf.append(c);
				}

			}
		}

		return stringBuf.toString();
	}
}