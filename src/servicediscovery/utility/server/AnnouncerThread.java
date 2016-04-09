package servicediscovery.utility.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import servicediscovery.utility.Protocol;

public class AnnouncerThread implements Runnable {
	
	private Integer PORT;
	private MulticastSocket socket;
	private boolean isRunning = true;
	
	public AnnouncerThread(Integer port){
		this.PORT = port;
	}

	@Override
	public void run() {
		try {
			this.socket = new MulticastSocket(PORT);
			socket.joinGroup(Protocol.getMulticastAddress());
			socket.setReuseAddress(true);
			DatagramPacket receivedPacket = Protocol.getNewEmptyDatagramPacket();
			
			while(isRunning){
				socket.receive(receivedPacket);
				System.out.println("Received discovery package from "+receivedPacket.getAddress()+". Responding now..");	
				byte[] buf = new byte[256];
				sendResponse(receivedPacket);
			}
			closeAll();
		} catch (SocketException e) {
			System.out.println("Could not create DatagramSocket.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendResponse(DatagramPacket pack){
		byte[] buf = new byte[256];
		DatagramSocket datagramSocket = null;
		DatagramPacket responsePacket = new DatagramPacket(buf, buf.length, pack.getAddress(), pack.getPort());
		try {
			datagramSocket = new DatagramSocket();
			datagramSocket.connect(pack.getAddress(), pack.getPort());
			datagramSocket.send(responsePacket);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (datagramSocket != null){
				datagramSocket.close();
			}
		}
		
	}
	
	public void stop(){
		this.isRunning = false;
	}
	
	private void closeAll(){
		this.socket.close();
	}

}
