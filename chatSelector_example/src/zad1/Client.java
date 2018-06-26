/**
 *
 *  @author Kobyliński Sławomir S12410
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class Client {
	private InetSocketAddress adres = new InetSocketAddress("localhost", 49998);
	private SocketChannel channel;
	private String nick = "";
	private static short nr = 0;
	private Komunikuj komunikacja = new Komunikuj();

	public Client() {
		// TODO Auto-generated constructor stub
		nr++;
		if (nick.isEmpty() || nick == null) {
			nick = JOptionPane.showInputDialog("Twój nick to ", "User#" + nr);
		}
		try {
			this.channel = SocketChannel.open(adres);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Klient " + this.nick + " chce nawiazac polaczenie");
		try {
			startClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Nie udalo sie uruchomic startClient()");
		}
	}

	public String getNick() {
		// TODO Auto-generated constructor stub
		return nick;
	}

	private void startClient() throws IOException {
		// TODO Auto-generated method stub
		if (!channel.isOpen()) {
			System.out.println("kanał jest zamknięty dla " + this.nick);
			return;
		}
		System.out.println("Client " + this.nick + " polaczony z  " + channel.getRemoteAddress().toString());
		this.nick = getNick();

		Gui gui = new Gui(this);
		while (true) {
			String wstep = new SimpleDateFormat("hh:mm:ss").format(new Date());
			String gadu = komunikacja.czytaj(this.channel);
			if (gadu.length() > 0) {
				gui.pokazGadu(wstep+" "+gadu);
			}
		}
	}
	public void zakonczDzialanieKlienta() {
		try {
			if(this.channel.isOpen()) {
				this.channel.close();
			}
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client();
	}

	public void wyslij(String text) {
		// TODO Auto-generated method stub
		if (text.length() > 0)
			komunikacja.wyslij(this.nick + " " + text, this.channel);
	}

}
