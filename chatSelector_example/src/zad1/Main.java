/**
 *
 *  @author Kobyliński Sławomir S12410
 *
 */

package zad1;

import java.io.IOException;



public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		
		new Thread(() -> {
			new Server();
		}).start();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		short LICZBA_KLIENTOW = 2;

		while (LICZBA_KLIENTOW-- > 0) {
			new Thread(() -> {
				new Client();}).start();
		}

	}

}
