package ie.swayne.net;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame extends JFrame implements ActionListener {
	
	JPanel container = new JPanel();
	
	JButton createBtn;
	JButton joinBtn;
	JButton refreshBtn;
	public Frame(String title) {
		super(title);
		init();
	}
	
	private void init() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	public Frame() {
		init();
	}
	
	private JPanel getCreateScreen() {
		JPanel panel = new JPanel();
		
		Server server = new Server();
		server.start();
		JLabel label = new JLabel("Created game on port " + server.getPort(), JLabel.CENTER);
		panel.add(label);
		return panel;
	}
	
	private JPanel getJoinedScreen(int port) {
		JPanel panel = new JPanel();
		
		JLabel label = new JLabel("Joined server " + port, JLabel.CENTER);
		panel.add(label);
		
		return panel;
	}
	
	private JPanel getJoinScreen() {
		JPanel panel = new JPanel();
		
		List<Server> servers = Globals.GET_SERVERS();
		
		JPanel buttonPanel = new JPanel(new GridLayout(servers.size(), 1));
		for(int i = 0;i < servers.size();i++) {
			JButton btn = new JButton("" + servers.get(i).getPort() + " (" + servers.get(i).getSize() + "/" + servers.get(i).getCapacity() + ")");
			btn.addActionListener(this);
			btn.setActionCommand("" + servers.get(i).getPort());
			buttonPanel.add(btn);
		} panel.add(buttonPanel);
		
		refreshBtn = new JButton("Refresh");
		refreshBtn.addActionListener(this);
		
		panel.add(refreshBtn);
		
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
			new Frame("Client");
		} else if(e.getSource() == joinBtn) {
			container.removeAll();
			container.add(getJoinScreen());
			container.revalidate();
			container.repaint();
			Frame.this.repaint();
			new Frame("Server");
		} else if(e.getSource() == refreshBtn) {
			container.removeAll();
			container.add(getJoinScreen());
			container.revalidate();
			container.repaint();	
			Frame.this.repaint();
		}
		
		else {
			int port = Integer.parseInt(((JButton)e.getSource()).getActionCommand());
			Client client = new Client(port);
			client.start();
			container.removeAll();
			container.add(getJoinedScreen(port));
			container.revalidate();
			container.repaint();
			Frame.this.repaint();
		}
	}
}
