/**
 *
 *  @author Kobyliński Sławomir S12410
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
	private ServerSocketChannel serverChannel = null;
	private Selector selector = null;
	private InetSocketAddress adres = new InetSocketAddress("localhost", 49998);
	private Komunikuj komunikacja = new Komunikuj();
	private String przeczytane = "";

	public Server() {
		try {
			serverChannel = ServerSocketChannel.open();// nowy kanał
			serverChannel.configureBlocking(false);// nieblokujący
			serverChannel.socket().bind(adres);// z kim i na czym;
			selector = Selector.open();
			// zarejestrowanie polaczenia
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Serwer dziala pod adresem >>" + adres.getHostName() + " na porcie " + adres.getPort());
		try {
			uslugaSerwera();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uslugaSerwera() throws IOException {
		// TODO Auto-generated method stub
		boolean serverDziala = true;
		while (serverDziala) {
			selector.select();// czeka na zajscie zdarzenia
			// cos sie stalo, zbior kluczy opisujacych zdarzenie
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iter = keys.iterator();
			while (iter.hasNext()) {// przegladamy klucze
				SelectionKey key = iter.next();
				// usuwamy ze zbiory ten ktorego uzyjemy
				iter.remove();
				if (key.isAcceptable()) {// jakis klient chce sie polaczyc
					SocketChannel cc = serverChannel.accept();
					if (cc != null) {
						cc.configureBlocking(false);
						// rejestrujemy kanal komunikacji z klientem do obslugi przez selektor
						// typ zdarzenia-dane gotowe do czytania
						cc.register(selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE));
					}
					System.out.println("Polaczenie zaakceptowane");
					continue;
				} else if (key.isReadable()) {
					// ktorys z kanalow gotowy do czytania
					// uzyskanie kanalu na ktorym czekaja dane
					SocketChannel cc = (SocketChannel) key.channel();
					System.out.println("Serwer czyta");
					przeczytane = komunikacja.czytaj(cc);
					// komunikacja.wyslijDoWszystkich(przeczytane, cc);
					continue;
				} else if (key.isWritable()) {
					SocketChannel cc = (SocketChannel) key.channel();
					if (przeczytane.length() > 0)
						for (SelectionKey k : selector.keys()) {
							if (k.isValid() && k.channel() instanceof SocketChannel) {
								SocketChannel ch = (SocketChannel) k.channel();
								komunikacja.wyslij(przeczytane, ch);
							}
						}
					przeczytane = "";
					continue;
				}
			}

		}
	}

	public static void main(String[] agrs) {
		new Server();
	}

}
