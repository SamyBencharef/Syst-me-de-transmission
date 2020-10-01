package codeur;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

/**
 * Class that defines the component Encoder. The input type is a Boolean and the output type is a Boolean. The class
 * implements the class Transmetteur.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class Codeur extends Transmetteur<Boolean, Boolean> {

    /**
     * The constructor initializes the mother class Transmetteur.
     */
    public Codeur() {
        super();
    }

    /**
     * Gets the Boolean information and stores in his attribute informationRecue. Then, encode the information and store
     * it to his attribute informationEmise and calls the method emettre().
     *
     * @param information (Information<Boolean>) Received information
     */
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        this.informationRecue = information;

        this.informationEmise = encoding(this.informationRecue);
        emettre();
    }

    /**
     * Emits information to all his interfaces in the attribute destinationsConnectees
     */
    @Override
    public void emettre() throws InformationNonConforme {
        // Send the message to the others destinations
        for (DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(this.informationEmise);
        }
    }

    /**
     * Gets the Boolean information and encodes it with the following rules : {0 -> 0 1 0, 1 -> 1 0 1}. Returns the
     * encoded information.
     *
     * @param information (Information<Boolean>) Information to encode
     * @return Information<Boolean>
     */
    private Information<Boolean> encoding(Information<Boolean> information) {
        Information<Boolean> encodedInformation = new Information<>();
        // Iterate though the information
        for (int i = 0; i < information.nbElements(); i++) {
            if (information.iemeElement(i)) {
                encodedInformation.add(true);
                encodedInformation.add(false);
                encodedInformation.add(true);
            } else {
                encodedInformation.add(false);
                encodedInformation.add(true);
                encodedInformation.add(false);
            }
        }
        return encodedInformation;
    }
}
