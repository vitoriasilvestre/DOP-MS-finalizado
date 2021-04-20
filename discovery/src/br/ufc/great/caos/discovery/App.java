package br.ufc.great.caos.discovery;

public class App {
	public static void main(String[] args) {
		// Discovery Cloudlet to clients
		Thread discoveryThread = new Thread(DiscoveryThread.getInstance());
		discoveryThread.start();
	}
}
