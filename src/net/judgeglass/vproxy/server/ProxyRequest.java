package net.judgeglass.vproxy.server;

import net.judgeglass.vproxy.server.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyRequest {
    private ServerSocket serverSocket;
    private int port;
    private boolean isRunning;
    
    public static String hostname = "tn.judgeglass.net";
    public static int hostport = 5209;

    public ProxyRequest(int port){
        this.port = port;
    }

    public void start(){
        isRunning = true;

        try {
            serverSocket = new ServerSocket(port);
            Logger.info("STARTED SERVER ON PORT " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(isRunning){
            try {
                Logger.info("Listening...");
                Socket c = serverSocket.accept();
                Logger.info("CLIENT CONNECTED FROM: " + c.getInetAddress());
                new ProxyClient(c).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean v){
        isRunning = v;
    }
}
