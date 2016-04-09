package servicediscovery.utility.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import servicediscovery.utility.Protocol;

public class DiscoveryThread implements Runnable {

	private Integer PORT;
	private ServiceLocator.OnServicesLocatedNotify serviceNotify;
	private DatagramSocket socket;
	
	
	public DiscoveryThread(Integer port, ServiceLocator.OnServicesLocatedNotify notifier){
		this.PORT = port;
		this.serviceNotify = notifier;
	}
	
	@Override
	public void run() {
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(10000);
			
			DatagramPacket pack = Protocol.getDiscoveryDatagramPacket(PORT);
			socket.send(pack);
			
			
			ArrayList<InetAddress> tmpReceivedResponses = new ArrayList<InetAddress>();
			try{
				do{
					DatagramPacket receivedPacket = Protocol.getNewEmptyDatagramPacket();
					socket.receive(receivedPacket);
					tmpReceivedResponses.add(receivedPacket.getAddress());
					System.out.println("DISCOVERY_THREAD: Received response from "+receivedPacket.getAddress());	
				}while(true);
			}catch (SocketTimeoutException e) {
			      System.out.println("Timeout");
		    }
			addressesLocated(tmpReceivedResponses);
			
		} catch (SocketException e) {
			System.out.println("Could not create Datagram socket!");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("Could not create socket. Unknown Host.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not send discovery message.");
			e.printStackTrace();
		}
		
	}
	
	private void addressesLocated(ArrayList<InetAddress> addresses){
		this.serviceNotify.onAddressesLocated(addresses);
	}

}
