package servicediscovery.utility.client;

import java.net.InetAddress;
import java.util.ArrayList;

public class ServiceLocator {
	
	/**
	 * Helper class for the thread to return the discovered addresses.
	 * @author dAmihl, Martin
	 *
	 */
	protected class OnServicesLocatedNotify{
		private ServiceLocator service;
		
		/**
		 * Constructor of sub class.
		 * @param service
		 */
		public OnServicesLocatedNotify(ServiceLocator service){
			this.service = service;
		}
		
		/**
		 * Method to set located addresses.
		 * @param locatedAddresses
		 */
		public void onAddressesLocated(ArrayList<InetAddress> locatedAddresses){
			this.service.setLocatedAddresses(locatedAddresses);
		}
	}
	
	private Integer PORT;
	private ArrayList<InetAddress> locatedAddresses;
	private final Object addressesLock = new Object();
	private Thread discoveryThread;
	
	/**
	 * Constructor.
	 * @param port
	 */
	public ServiceLocator(Integer port){
		this.PORT = port;
		this.locatedAddresses = new ArrayList<>();
	}
	
	/**
	 * Gets located addresses.
	 * @return located addresses
	 */
	public ArrayList<InetAddress> getLocatedAddresses(){
		synchronized(addressesLock){
			return locatedAddresses;
		}
	}
	
	/**
	 * Sets located addresses.
	 * @param newAddresses
	 */
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
		  discoveryThread = new Thread(new DiscoveryThread(this.PORT, new OnServicesLocatedNotify(this)));
		  discoveryThread.start();
	}
}
