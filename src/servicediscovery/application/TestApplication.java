package servicediscovery.application;

import servicediscovery.utility.client.ServiceLocator;
import servicediscovery.utility.server.ServiceAnnouncer;

public class TestApplication {

	private static int TEST_PORT = 1337;
	
	 public static void main(String[] args) {		
		if (args.length < 1){
			System.out.println("No Arguments found. Running as client.");
			runAsClient();
		}else{
			String arg = args[0].toLowerCase();
			
			if (arg.equals("server")){
				runAsServer();
			}else{
				runAsClient();
			}
		}
		
	}
	 
	 private static void runAsServer(){
		 ServiceAnnouncer announcer = new ServiceAnnouncer(TEST_PORT);
		 announcer.start();
	 }
	 
	 private static void runAsClient(){
		 ServiceLocator locator = new ServiceLocator(TEST_PORT);
		 locator.startDiscovery();
	 }
}
