package servicediscovery.utility.client;

import java.net.InetAddress;
import java.util.ArrayList;

public class ServiceLocator {

	
	/**
	 * Helper class for the thread to return the discovered addresses.
	 * @author dAmihl
	 *
	 */
	protected class OnServicesLocatedNotify{

		private ServiceLocator service;
		
		public OnServicesLocatedNotify(ServiceLocator service){
			this.service = service;
		}
		
		public void onAddressesLocated(ArrayList<InetAddress> locatedAddresses){
			this.service.setLocatedAddresses(locatedAddresses);
		}
		
	}
	
	
	private Integer PORT;
	private ArrayList<InetAddress> locatedAddresses;
	private final Object addressesLock = new Object();
	private Thread discoveryThread;
	
	
	public ServiceLocator(Integer port){
		this.PORT = port;
		this.locatedAddresses = new ArrayList<>();
	}
	
	public ArrayList<InetAddress> getLocatedAddresses(){
		synchronized(addressesLock){
			return locatedAddresses;
		}
	}
	
	private void setLocatedAddresses(ArrayList<InetAddress> newAddresses){
		synchronized(addressesLock){
			locatedAddresses.clear();
			locatedAddresses.addAll(newAddresses);
		}
	}
	
	/**
	 * Starts a thread which will discover the network. When finished, the OnServicesLocatedNotify will
	 * fire and set the located addresses in locatedAddresses.
	 */
	public void startDiscovery(){
		  discoveryThread = new Thread(
				  new DiscoveryThread(this.PORT, new OnServicesLocatedNotify(this)));
		  discoveryThread.start();
	}
	
	
	
	
}
