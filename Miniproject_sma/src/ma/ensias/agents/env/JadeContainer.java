package ma.ensias.agents.env;

import jade.wrapper.ContainerController;
import jade.core.Runtime ; 
import jade.core.ProfileImpl;

public class JadeContainer {
	private ContainerController container;
	public JadeContainer() {
		Runtime rt = Runtime.instance() ; //Créer une instance de la MV
		//Pas de propriétés, ce n’est pas un main container, mais un profile !
		ProfileImpl profile = new ProfileImpl(false);
		//Le main container associé est déjà démarré sur localhost
		profile.setParameter(ProfileImpl.MAIN_HOST, "localhost") ;
		profile.setParameter(ProfileImpl.CONTAINER_NAME, "Traffic") ;
		container = rt.createAgentContainer(profile); //Créer le container
		
	}
	public ContainerController getContainer(){ return container; }
}
