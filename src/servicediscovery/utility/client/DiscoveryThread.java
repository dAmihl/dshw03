package servicediscovery.utility.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import servicediscovery.utility.Protocol;

/**
 * Thread to discover servers.
 * @author dAmihl, Martin
 *
 */

public class DiscoveryThread implements Runnable {
	private Integer PORT;
	private ServiceLocator.OnServicesLocatedNotify serviceNotify;
	private DatagramSocket socket;
	
	private Integer TIMEOUT = 5000;
	
	/**
	 * Constructor.
	 * @param port
	 * @param notifier
	 */
	public DiscoveryThread(Integer port, ServiceLocator.OnServicesLocatedNotify notifier){
		this.PORT = port;
		this.serviceNotify = notifier;
	}
	
	/**
	 * Executes the run method of the thread.
	 */
	@Override
	public void run() {
		try {
			// create a new datagram socket and set the timeout
			socket = new DatagramSocket();
			socket.setSoTimeout(TIMEOUT);
			
			// create a package and send it
			DatagramPacket pack = Protocol.getDiscoveryDatagramPacket(PORT);
			socket.send(pack);
			
			// create an array list of internet addresses where received responses are stored
			ArrayList<InetAddress> tmpReceivedResponses = new ArrayList<InetAddress>();
			try{
				// try to receive a package and discover its address.
				do{
					DatagramPacket receivedPacket = Protocol.getNewEmptyDatagramPacket();
					socket.receive(receivedPacket);
					tmpReceivedResponses.add(receivedPacket.getAddress());
					System.out.println("DISCOVERY_THREAD: Received response from "+receivedPacket.getAddress());	
				}while(true);
			}catch (SocketTimeoutException e) {
			      System.out.println("Timeout.");
		    }catch (Exception e){
		    	this.serviceNotify.onThreadShutdown();
		    	return;
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
	
	/**
	 * Checks if adresses are located.
	 * @param addresses
	 */
	private void addressesLocated(ArrayList<InetAddress> addresses){
		this.serviceNotify.onAddressesLocated(addresses);
	}
}
