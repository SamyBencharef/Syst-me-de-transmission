package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import sources.SourceInterface;

import java.util.LinkedList;

public class TransmetteurParfaitAnalogique extends Transmetteur<Float, Float> {

	@Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        this.informationRecue = information;
        emettre();
    }

	@Override
    public void emettre() throws InformationNonConforme {
        this.informationEmise = this.informationRecue;
        // Send the message to the others destinations
        for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(this.informationEmise);
        }
    }
	
}
