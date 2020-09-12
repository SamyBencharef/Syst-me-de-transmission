package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

/**
 * Class that defines a perfect logical transmitter (receive and send the same message wihout adding noise).
 * The input type is a Boolean and the output type is a Boolean.
 * The class implements the class Transmetteur.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh le HUY - Milo THIBAUD - Lucas BERENGUER
 */
public class TransmetteurParfaitLogique extends Transmetteur<Boolean, Boolean> {

    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        // Set the information
        this.informationRecue = information;
        // Send the information to all connected components
        emettre();
    }

    @Override
    public void emettre() throws InformationNonConforme {
        this.informationEmise = this.informationRecue;
        // Send the message to the others destinations
        for (DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(this.informationEmise);
        }
    }
}
