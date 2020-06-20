package net.judgeglass.vproxy.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import net.judgeglass.vproxy.server.utils.Logger;

public class Connection {
	private String hostname;
	private int port;
	private String externHost;
	private int externPort;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	private Socket proxyServer;
	private Window window;
	
	public Connection(String hostname, int port, String externHost, int externPort, Window window) {
		this.hostname = hostname;
		this.port = port;
		this.externHost = externHost;
		this.externPort = externPort;
		this.window = window;
	}
	
	public void sendConfigRequest(String command) {
		if(command.equals("VPROXY_REQUEST_HOST_CHANGE")) {
			command += ":" + externHost + ":" + externPort;
		}
		
		try {
			proxyServer = new Socket(hostname, port);
			inputStream = proxyServer.getInputStream();
			outputStream = proxyServer.getOutputStream();
			
			new Thread(() -> serverResponse()).start();
			
			PrintWriter pw = new PrintWriter(outputStream);
			pw.println(command);
			
			pw.flush();
			
		}catch(IOException e) {
			Logger.error(e.getMessage());
			window.appendToLog("Failed to connect to: " + hostname + ":" + port);
		}
		
	}
	
	private void serverResponse() {
		int bytes;
		final byte[] response = new byte[4096];
		try {
			while((bytes = inputStream.read(response)) != -1) {
				String flag = new String(response, StandardCharsets.UTF_8);
				flag = flag.trim();
				if(flag.equals("VPROXY_REQUEST_HOST_CHANGE_1")) {
					window.appendToLog("Succsess!: " + hostname + ":" + port + " -> " + externHost + ":" + externPort);
				}
			}
			inputStream.close();
			proxyServer.close();
		}catch(IOException e) {
			Logger.error(e.getMessage());
		}
	}
}
