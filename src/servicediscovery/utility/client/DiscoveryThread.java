package servicediscovery.utility.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import servicediscovery.utility.Protocol;

public class DiscoveryThread implements Runnable {

	private Integer PORT;
	private ServiceLocator.OnServicesLocatedNotify serviceNotify;
	private DatagramSocket socket;
	private final String MULTICAST_ADDRESS = "255.255.255.255";
	
	public DiscoveryThread(Integer port, ServiceLocator.OnServicesLocatedNotify notifier){
		this.PORT = port;
		this.serviceNotify = notifier;
	}
	
	@Override
	public void run() {
		try {
			socket = new DatagramSocket(PORT, InetAddress.getByName(MULTICAST_ADDRESS));
			socket.send(Protocol.getDiscoveryDatagramPacket());
			
			ArrayList<InetAddress> tmpReceivedResponses = new ArrayList<InetAddress>();
			
			DatagramPacket receivedPacket = Protocol.getNewEmptyDatagramPacket();
			socket.receive(receivedPacket);
			tmpReceivedResponses.add(receivedPacket.getAddress());
			System.out.println("DISCOVERY_THREAD: Received response from "+receivedPacket.getAddress());	
			
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
