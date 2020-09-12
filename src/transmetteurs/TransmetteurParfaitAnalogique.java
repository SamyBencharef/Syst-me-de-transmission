package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

/**
 * Class that defines a perfect analogical transmitter (receive and send the same message wihout adding noise). The
 * input type is a Float and the output type is a Float. The class implements the class Transmetteur.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh le HUY - Milo THIBAUD - Lucas BERENGUER
 */
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
