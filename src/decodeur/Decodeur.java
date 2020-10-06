package decodeur;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

/**
 * Class that defines the component Decoder. The input type is a Boolean and the output type is a Boolean. The class
 * implements the class Transmetteur.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class Decodeur extends Transmetteur<Boolean, Boolean> {

    /**
     * The constructor initializes the mother class Transmetteur.
     */
    public Decodeur() {
        super();
    }

    /**
     * Gets the Boolean information and stores in his attribute informationRecue. Then, decode the information and store
     * it to his attribute informationEmise and calls the method emettre().
     *
     * @param information (Information<Boolean>) Received information
     */
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        this.informationRecue = information;
        this.informationEmise = decoding(this.informationRecue);

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
     * Gets the Boolean information and decodes it with the following rules : {(0 0 0), (0 1 0), (0 1 1), (1 1 0) -> 0
     * ;(0 0 1), (1 0 0), (1 0 1), (1 1 1) -> 1}. Returns the decoded information.
     *
     * @param information (Information<Boolean>) Information to decode
     * @return Information<Boolean>
     */
    private Information<Boolean> decoding(Information<Boolean> information) throws InformationNonConforme {
        Information<Boolean> decodedInformation = new Information<>();
        // Throw an execution if information's size is incorrect
        if ((information.nbElements() % 3) != 0) {
            throw new InformationNonConforme("The" + " " + "size of the information doesn't match with a coded " +
                    "information");
        }
        // Iterate though the information
        for (int i = 0; i < information.nbElements(); i += 3) {
            if (information.iemeElement(i + 1) || (!information.iemeElement(i) && !information.iemeElement(i + 2))) {
                decodedInformation.add(false);
            } else {
                decodedInformation.add(true);
            }
        }
        return decodedInformation;
    }
}
