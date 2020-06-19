package net.judgeglass.vproxy.server;

import net.judgeglass.vproxy.server.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProxyClient extends Thread{
    private Socket client;
    private Socket remoteServer;

    private InputStream clientInput;
    private OutputStream clientOutput;
    private InputStream remoteInput;
    private OutputStream remoteOutput;

    private String proxyRemoteHost = "tn.judgeglass.net";
    private final byte[] requestArray = new byte[1024];
    private final byte[] replyArray = new byte[4096];

    public ProxyClient(Socket client){
        this.client = client;
    }

    public void run(){
        try {
            startConnection();
        } catch (IOException e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void startConnection() throws IOException {
        clientInput = client.getInputStream();
        clientOutput = client.getOutputStream();

        remoteServer = new Socket(proxyRemoteHost, 5209);
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

    private void sendRemoteData(){
        int bytes;
        try {
            while ((bytes = clientInput.read(requestArray)) != -1) {
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
