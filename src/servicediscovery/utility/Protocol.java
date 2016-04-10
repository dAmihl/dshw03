package servicediscovery.utility;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Protocol class.
 * @author dAmihl, Martin
 *
 */

public class Protocol {
	public static final String DISCOVERY_MESSAGE = new String("discovery");
	public static final int DATAGRAM_PACKET_LENGTH = 10;
	public static final String MULTICAST_IP_STRING = "230.1.1.1";
	
	/**
	 * Method to generate a new datagram package.
	 * 
	 * @return new DatagramPacket
	 */
	public static DatagramPacket getNewEmptyDatagramPacket(){
		byte[] buf = new byte[256];
		DatagramPacket newPack = new DatagramPacket(buf, buf.length);
		return newPack;
	}
	
	/**
	 * Method to generate a new discovery package with the given port.
	 * 
	 * @param port
	 * @return discoveryPack
	 */
	public static DatagramPacket getDiscoveryDatagramPacket(Integer port){
		byte[] buf = DISCOVERY_MESSAGE.getBytes();
		DatagramPacket discoveryPack;
		
		discoveryPack = new DatagramPacket(buf, buf.length, getMulticastAddress(), port);
		return discoveryPack;
	}
	
	/**
	 * Method to get the Multicast IP address.
	 * 
	 * @return multicast IP address on success, "unknown host" else.
	 */
	public static InetAddress getMulticastAddress(){
		try {
			return InetAddress.getByName(MULTICAST_IP_STRING);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host.");
			e.printStackTrace();
		}
		return null;
	}
}
