package ie.swayne.net;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame extends JFrame implements ActionListener {
	
	JPanel container = new JPanel();
	
	JButton createBtn;
	JButton joinBtn;
	
	public Frame() {
		this.add(container);
		
		createBtn = new JButton("Create");
		createBtn.addActionListener(this);
		joinBtn = new JButton("Join");
		joinBtn.addActionListener(this);
		container.add(createBtn);
		container.add(joinBtn);
		this.setVisible(true);
		this.pack();
	}
	
	private JPanel getCreateScreen() {
		JPanel panel = new JPanel();
		
		Server server = new Server();
		server.start();
		JLabel label = new JLabel("Created game on port " + server.getPort(), JLabel.CENTER);
		panel.add(label);
		return panel;
	}
	
	private JPanel getJoinScreen() {
		JPanel panel = new JPanel();
		
		Client client = new Client(Globals._PORT);
		client.start();
		
		return panel;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createBtn) {
			container.removeAll();
			container.add(getCreateScreen());
			container.revalidate();
			container.repaint();
			Frame.this.repaint();
		} else if(e.getSource() == joinBtn) {
			container.removeAll();
			container.add(getJoinScreen());
			container.revalidate();
			container.repaint();
			Frame.this.repaint();
		}
	}
}
