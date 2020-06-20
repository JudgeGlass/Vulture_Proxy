package net.judgeglass.vproxy;

import net.judgeglass.vproxy.config.Window;
import net.judgeglass.vproxy.server.ProxyRequest;
import net.judgeglass.vproxy.server.utils.Logger;

public class Main {
	
	private static boolean isConfig = true;
	
    public static void main(String args[]){
    	if(args.length > 0) {
    		if(args[0].equals("gui")) {
    			new Thread(() -> new Window()).start();
    		}
    	}
    	
        int port = 5209;
        ProxyRequest pr = new ProxyRequest(port);
        pr.start();
    }
}
