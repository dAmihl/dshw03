package servicediscovery.utility.server;

public class ServiceAnnouncer {
	
	private Integer PORT;
	private Thread announcerThread;
	
	public ServiceAnnouncer(Integer port){
		this.PORT = port;
	}
	
	public void start(){
		
		this.announcerThread = new Thread(new AnnouncerThread(this.PORT));
		this.announcerThread.start();
	}
	
	public void shutdown(){
		if (this.announcerThread != null){
			// NEEDS A BETTER WAY!
			this.announcerThread.interrupt();
		}
	}

}
