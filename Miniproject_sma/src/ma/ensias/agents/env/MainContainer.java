package ma.ensias.agents.env;

import jade.wrapper.ContainerController;
import jade.core.Runtime ; 
import jade.core.ProfileImpl;
import jade.util.leap.Properties ; 
import jade.util.ExtendedProperties ;

public class MainContainer {
	private ContainerController mainContainer;
	public MainContainer() {
		try {
			Runtime runtime = Runtime.instance() ; //Créer une instance de la MV
			Properties properties = new ExtendedProperties() ; //fixer quelques propriétés
			properties.setProperty("gui","true") ; // ... le -gui
			ProfileImpl profileImpl = new ProfileImpl(properties);
			mainContainer = runtime.createMainContainer(profileImpl); //créer le main-container
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	public ContainerController getContainer(){ return mainContainer; }
}