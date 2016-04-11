package servicediscovery.utility.server;

/**
 * Helper class for the thread to announce the discovered addresses.
 * @author dAmihl, Martin
 *
 */
public class ServiceAnnouncer {
	private Integer PORT;
	private Thread announcerThread;
	
	/**
	 * Constructor.
	 * @param port
	 */
	public ServiceAnnouncer(Integer port){
		this.PORT = port;
		addShutdownHook();
	}
	
	/**
	 * Method starts the thread.
	 */
	public void start(){
		this.announcerThread = new Thread(new AnnouncerThread(this.PORT));
		this.announcerThread.start();
		
	}
	
	/**
	 * Adds a shutdown hook to gracefully shutdown service announcer..
	 */
	private void addShutdownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
            public void run()
            {
				System.out.println("Gracefully shutting down the ServiceAnnouncer..");
                shutdown();
            }
		});
	}
	
	
	/**
	 * Method ends the thread.
	 */
	public void shutdown(){
		if (this.announcerThread != null){
			this.announcerThread.interrupt();
		}
	}
	
	

}
