package servicediscovery.utility;

import java.net.DatagramPacket;

public class Protocol {
	
	public static final String DISCOVERY_MESSAGE = new String("discovery");
	public static final int DATAGRAM_PACKET_LENGTH = 10;
	
	public static DatagramPacket getNewEmptyDatagramPacket(){
		DatagramPacket newPack = new DatagramPacket(new byte[DATAGRAM_PACKET_LENGTH], DATAGRAM_PACKET_LENGTH);
		return newPack;
	}
	
	public static DatagramPacket getDiscoveryDatagramPacket(){
		DatagramPacket discoveryPack = new DatagramPacket(DISCOVERY_MESSAGE.getBytes(), DATAGRAM_PACKET_LENGTH);
		return discoveryPack;
	}

}
