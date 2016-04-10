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
	}
	
	/**
	 * Method starts the thread.
	 */
	public void start(){
		this.announcerThread = new Thread(new AnnouncerThread(this.PORT));
		this.announcerThread.start();
	}
	
	/**
	 * Method ends the thread.
	 */
	public void shutdown(){
		if (this.announcerThread != null){
			// try to end thread
			try {
				this.announcerThread.join(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
