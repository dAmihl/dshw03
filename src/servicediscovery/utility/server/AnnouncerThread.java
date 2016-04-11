package servicediscovery.utility.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;

import servicediscovery.utility.Protocol;

/**
 * Thread to announce discoveries.
 * @author dAmihl, Martin
 *
 */
public class AnnouncerThread implements Runnable {
	private Integer PORT;
	private MulticastSocket socket;
	private boolean isRunning = true;
	
	/**
	 * Constructor.
	 * @param port
	 */
	public AnnouncerThread(Integer port){
		this.PORT = port;
	}
	
	/**
	 * Executes the run method of the thread thread.
	 */
	@Override
	public void run() {
		try {
			// create socket and datagram package
			this.socket = new MulticastSocket(PORT);
			socket.joinGroup(Protocol.getMulticastAddress());
			socket.setReuseAddress(true);
			DatagramPacket receivedPacket = Protocol.getNewEmptyDatagramPacket();
			
			// try to receive packages
			while(isRunning){
				socket.receive(receivedPacket);
				System.out.println("Received discovery package from "+receivedPacket.getAddress()+".\n Responding now...");	
				sendResponse(receivedPacket);
			}
			
			// close socket
			closeAll();
		} catch (SocketException e) {
			System.out.println("Could not create DatagramSocket.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method sends response packages.
	 * @param pack
	 */
	private void sendResponse(DatagramPacket pack){
		byte[] buf = new byte[256];
		DatagramSocket datagramSocket = null;
		DatagramPacket responsePacket = new DatagramPacket(buf, buf.length, pack.getAddress(), pack.getPort());
		try {
			datagramSocket = new DatagramSocket();
			datagramSocket.connect(pack.getAddress(), pack.getPort());
			datagramSocket.send(responsePacket);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (datagramSocket != null){
				datagramSocket.close();
			}
		}
		
	}
	
	/**
	 * Closes used socket.
	 */
	private void closeAll(){
		this.socket.close();
	}
}
