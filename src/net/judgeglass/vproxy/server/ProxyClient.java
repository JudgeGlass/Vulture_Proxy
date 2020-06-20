package net.judgeglass.vproxy.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import net.judgeglass.vproxy.server.utils.Logger;

public class ProxyClient extends Thread{
    private Socket client;
    private Socket remoteServer;

    private InputStream clientInput;
    private OutputStream clientOutput;
    private InputStream remoteInput;
    private OutputStream remoteOutput;

    private String proxyRemoteHost = "www.cplusplus.com";
    private final byte[] requestArray = new byte[1024];
    private final byte[] replyArray = new byte[4096];

    public ProxyClient(Socket client){
        this.client = client;
        Logger.info("CONNECTION: " + ProxyRequest.hostname + ":" + ProxyRequest.hostport);
    }

    public void run(){
        try {
            startConnection();
        } catch (IOException e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
            try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }

    private void startConnection() throws IOException {
        clientInput = client.getInputStream();
        clientOutput = client.getOutputStream();
        
        remoteServer = new Socket(ProxyRequest.hostname, ProxyRequest.hostport);
        remoteInput = remoteServer.getInputStream();
        remoteOutput = remoteServer.getOutputStream();

        new Thread(() -> sendRemoteData()).start();
        readRemoteData();
    }

    private void readRemoteData(){
        int bytes;
        try{
            while((bytes = remoteInput.read(replyArray)) != -1){
                clientOutput.write(replyArray, 0, bytes);
                clientOutput.flush();
            }

            client.close();
        }catch (IOException e){
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void writeUTF(String msg) {
    	PrintWriter pw = new PrintWriter(clientOutput);
    	pw.println(msg);
    	pw.flush();
    }

    private void sendRemoteData(){
        int bytes;
        try {
            while ((bytes = clientInput.read(requestArray)) != -1) {
            	String httpCommand = new String(requestArray);
            	httpCommand = httpCommand.trim();
            	if(httpCommand.contains("VPROXY_REQUEST_HOST_CHANGE")) {
            		String data[] = httpCommand.split(":");
            		ProxyRequest.hostname = data[1];
            		ProxyRequest.hostport = Integer.valueOf(data[2]);
            		Logger.info("External Host is now: " + ProxyRequest.hostname + ":" + ProxyRequest.hostport);
            		writeUTF("VPROXY_REQUEST_HOST_CHANGE_1");
            		//return;
            	}
            	
            	
                remoteOutput.write(requestArray, 0, bytes);
                remoteOutput.flush();
            }

            remoteServer.close();
        }catch (IOException e){
            Logger.error(e.getMessage());
            e.getStackTrace();
        }
    }
}
