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
			this.service.onDiscoveryFinished();
		}
		
		/**
		 * Called when the thread is shutdown without finishing discovery.
		 */
		public void onThreadShutdown(){
			this.service.setDiscoveryFinished(true);
			this.service.discoveryThread = null;
		}
	}

	
	private Integer PORT;
	private ArrayList<InetAddress> locatedAddresses;
	private final Object addressesLock = new Object();
	private Thread discoveryThread;
	
	private final Object isFinishedLock = new Object();
	private boolean isDiscoveryFinished = false;
	
	private DiscoveryNotify discoveryNotifier;
	
	/**
	 * Constructor.
	 * @param port
	 */
	public ServiceLocator(Integer port){
		this.PORT = port;
		this.locatedAddresses = new ArrayList<>();
		addShutdownHook();
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
	 * @return True, if the discovery thread started successfully; False, if there's already a discovery
	 * thread running.
	 */
	private boolean startDiscovery(){
		if (discoveryThread == null){
		  discoveryThread = new Thread(new DiscoveryThread(this.PORT, new OnServicesLocatedNotify(this)));
		  discoveryThread.start();
		  setDiscoveryFinished(false);
		  return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Blocking operation. Starts discovery and blocks until discovery is done.
	 * @return success.
	 */
	public boolean startBlockingDiscovery(){
		boolean result = startDiscovery();
		if (!result) return false;
		while (this.getDiscoveryFinished() == false);
		return true;
	}
	
	/**
	 * Starts discovery and notifes the notifyObject when finished
	 * @param notifyObject the notify object to call
	 * @return success.
	 */
	public boolean startDiscoveryWithNotify(DiscoveryNotify notifyObject){
		this.discoveryNotifier = notifyObject;
		return startDiscovery();
	}
	
	/**
	 * Adds a shutdown hook to the runtime to gracefully shutdown.
	 */
	private void addShutdownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
            public void run()
            {
				System.out.println("Gracefully shutting down the ServiceLocator..");
                shutdown();
            }
		});
	}
	
	
	/**
	 * Shutdown of the thread
	 */
	private void shutdown(){
		if (this.discoveryThread != null){
			try {
				this.discoveryThread.join(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Called when the discovery thread is done (e.g. the socket timeout occured and all discovered IP
	 * addresses are saved.
	 */
	public void onDiscoveryFinished(){
		System.out.println("Discovery Finished:");
		System.out.println(this.locatedAddresses);
		this.setDiscoveryFinished(true);
		this.discoveryThread = null;
		if (discoveryNotifier != null){
			this.discoveryNotifier.onDiscoveryFinished();
		}
	}
	
	
	/**
	 * Sets a lock on the boolean of isDiscoveryFinished and sets the new value
	 * @param finished the new value of isDiscoveryFinished
	 */
	private void setDiscoveryFinished(boolean finished){
		synchronized(isFinishedLock){
			this.isDiscoveryFinished = finished;
		}
	}
	
	/**
	 * Trys to get the lock on isDiscoveryFInished and returns its value.
	 * @return
	 */
	public boolean getDiscoveryFinished(){
		synchronized(isFinishedLock){
			return this.isDiscoveryFinished;
		}
	}
	
	
}
