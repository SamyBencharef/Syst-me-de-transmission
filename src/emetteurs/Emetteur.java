package emetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

public class Emetteur extends Transmetteur<Boolean, Float>  {

	private String codeType;
	public Emetteur(String param) {
		super();
		codeType=param;
				
	}
	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConforme {
		this.informationRecue = information;
        emettre();
	}

	@Override
	public void emettre() throws InformationNonConforme {
		
        // Send the message to the others destinations
        for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(this.informationEmise);
        }
	}
	
	private void logicToNrz(Information<Boolean> information) {
		// transfo 0101... to NRZ code
		
		
	}
	
	
	
	

}
