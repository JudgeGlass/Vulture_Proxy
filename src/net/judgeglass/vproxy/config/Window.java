package net.judgeglass.vproxy.config;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Window {
	private JFrame frame;
	private JLabel lblHostname;
	private JLabel lblPort;
	private JLabel lblExternHost;
	private JLabel lblExternPort;
	private JTextField txtHostname;
	private JTextField txtExternHost;
	private JTextField txtPort;
	private JTextField txtExternPort;
	private JButton btnSend;
	private JTextArea txtLog;

	
	public Window() {
		frame = new JFrame("Vulture Proxy Config");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(360, 300);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		
		addComponents();
		
		frame.setVisible(true);
	}
	
	private void addComponents() {
		lblHostname = new JLabel("Hostname:");
		lblHostname.setBounds(5, 5, 160, 15);
		frame.getContentPane().add(lblHostname);
		
		txtHostname = new JTextField("localhost");
		txtHostname.setBounds(5, 25, 160, 25);
		frame.getContentPane().add(txtHostname);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(5, 55, 160, 15);
		frame.getContentPane().add(lblPort);
		
		txtPort = new JTextField("3030");
		txtPort.setBounds(5, 75, 160, 25);
		frame.getContentPane().add(txtPort);
		
		lblExternHost = new JLabel("External Hostname:");
		lblExternHost.setBounds(180, 5, 160, 15);
		frame.getContentPane().add(lblExternHost);
		
		txtExternHost = new JTextField("judgeglass.net");
		txtExternHost.setBounds(180, 25, 160, 25);
		frame.getContentPane().add(txtExternHost);
		
		lblExternPort = new JLabel("External Port:");
		lblExternPort.setBounds(180, 55, 160, 15);
		frame.getContentPane().add(lblExternPort);
		
		txtExternPort = new JTextField("25565");
		txtExternPort.setBounds(180, 75, 160, 25);
		frame.getContentPane().add(txtExternPort);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(180, 105, 160, 30);
		btnSend.addActionListener((ActionEvent) -> send());
		frame.getContentPane().add(btnSend);
		
		txtLog = new JTextArea();
		txtLog.setBounds(5, 140, 335, 120);
		txtLog.append("Vulture Config Log\n----------------------------\n");
		frame.getContentPane().add(txtLog);
	}
	
	private void send() {
		String hostname = txtHostname.getText();
		int port = Integer.parseInt(txtPort.getText());
		String externHost = txtExternHost.getText();
		int externPort = Integer.parseInt(txtExternPort.getText());
		Connection connection = new Connection(hostname, port, externHost, externPort, this);
		connection.sendConfigRequest("VPROXY_REQUEST_HOST_CHANGE");
	}
	
	public void appendToLog(String messsage) {
		txtLog.append(messsage + "\n");
	}
}
