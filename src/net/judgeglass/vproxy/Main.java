package net.judgeglass.vproxy;

import net.judgeglass.vproxy.server.ProxyRequest;

public class Main {
    public static void main(String args[]){
        int port = 3030;
        ProxyRequest pr = new ProxyRequest(port);
        pr.start();
    }
}
