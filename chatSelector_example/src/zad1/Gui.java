package zad1;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gui extends JPanel implements ActionListener {
	protected JTextField poleDoPisania;
	protected JTextArea pokazGadu;
	private JButton logoutButton;
	private final static String NEXT_LINE = "\n";
	private Client client;

	public Gui(Client c) {
		super(new BorderLayout(0, 0));
		this.client = c;
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				start();
			}
		});
		
	}

	protected void start() {
		// TODO Auto-generated method stub
		poleDoPisania = new JTextField(20);
		poleDoPisania.addActionListener(this);
		pokazGadu = new JTextArea(5, 5);
		pokazGadu.setEditable(false);
		pokazGadu.setBackground(Color.GRAY);
		pokazGadu.setLineWrap(true);
		logoutButton = new JButton("Wyloguj: "+this.client.getNick());
		logoutButton.setActionCommand("logout");
		logoutButton.addActionListener(this);
		JScrollPane scrollPane = new JScrollPane(pokazGadu);
		setPreferredSize (new Dimension (400, 200));
		add(logoutButton, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(poleDoPisania, BorderLayout.SOUTH);
		logoutButton.setBackground(Color.ORANGE);
		this.stworzGui();
	}

	public void pokazGadu(String text){
		pokazGadu.append(text + NEXT_LINE);
		pokazGadu.setCaretPosition(pokazGadu.getDocument().getLength());
		pokazGadu.update(pokazGadu.getGraphics());
	}
	public void actionPerformed(ActionEvent evt) {
		if ("logout".equals(evt.getActionCommand())) {
			client.zakonczDzialanieKlienta();
			frame.setVisible( false );
			frame.dispose();
			//System.exit(0);
		}
		if(poleDoPisania.getText().length() > 0)
			this.client.wyslij(poleDoPisania.getText());
		poleDoPisania.setText("");
		
	}
	JFrame frame;
	public void stworzGui() {
		frame = new JFrame("TPO3 s12410 - CHAT");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
}
