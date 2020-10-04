package recepteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that defines the component receiver. The input type is a Float and the output type is a Boolean. The class
 * implements the class Transmetteur.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class Recepteur extends Transmetteur<Float, Boolean> {

    private final String codeType;
    private final Float ampliMax;
    private final Float ampliMin;
    private final Float seuilMax;
    private final Float seuilMin;
    private final Integer nbEchTpsBit;
    private final ArrayList<Integer> dt;
    private final ArrayList<Float> ar;
    private final Boolean fixMultiPaths;
    private int nbElements;

    /**
     * The constructor sets the attributes in order to convert the information.
     *
     * @param codetype      (String) code type of the received information
     * @param nbEchTpsBit   (Integer) number of samples per bit
     * @param ampliMax      (Float) Value max of the amplitude from the signal
     * @param ampliMin      (Float) Value min of the amplitude from the signal
     * @param dt            (ArrayList<Integer>) Delay of the indirect Signals
     * @param ar            (ArrayList<Float>) Attenuation of the indirect Signals amplitude
     * @param fixMultiPaths (Boolean) Indicate the use of the correction for multi paths
     */
    public Recepteur(String codetype, Integer nbEchTpsBit, Float ampliMax, Float ampliMin, ArrayList<Integer> dt,
                     ArrayList<Float> ar, Boolean fixMultiPaths) {
        super();
        this.codeType = codetype;
        this.nbEchTpsBit = nbEchTpsBit;
        this.ampliMax = ampliMax;
        this.ampliMin = ampliMin;
        this.seuilMax = ampliMax;
        this.seuilMin = ampliMin;
        this.dt = (ArrayList<Integer>) dt.clone();
        this.ar = (ArrayList<Float>) ar.clone();
        this.fixMultiPaths = fixMultiPaths;
        this.nbElements = 0;
    }

    /**
     * Gets the Float information and stores in his attribute informationRecue. Then, convert the information to Boolean
     * and calls the method emettre().
     *
     * @param information (Information<Float>) Information to convert
     */
    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        this.informationRecue = information;
        this.nbElements = information.nbElements() - Collections.max(dt);
        if (dt.get(0) != 0 && ar.get(0) != 0f && fixMultiPaths) {
            information = fixMultiPaths(information);
        }

        switch (codeType) {
            case "NRZ":
                this.informationEmise = nrzToLogic(information);
                break;
            case "NRZT":
                this.informationEmise = nrztToLogic(information);
                break;
            case "RZ":
                this.informationEmise = rzToLogic(information);
                break;
            default:
                throw new InformationNonConforme("Wrong Code Type given : " + codeType);
        }
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
     * Converts the analogical information coded in NRZ (Non Return to Zero) into logical information
     *
     * @param information (Information<Float>) Logical Information that contains the data
     * @return Information<Boolean>
     */
    private Information<Boolean> nrzToLogic(Information<Float> information) {
        // Construct the new Information<Boolean>
        Information<Boolean> newInformation = new Information<>();
        // Iterate through the variable Information<Float>
        for (int i = 0; i < nbElements; i = i + nbEchTpsBit) {
            float mean = 0.00f;
            for (int n = 0; n < nbEchTpsBit; n++) {
                mean += information.iemeElement(i + n);
            }
            mean = mean / nbEchTpsBit;
            // Quantify
            if (compareValues(seuilMin, seuilMax, mean) == seuilMax) {
                newInformation.add(true);
            } else {
                newInformation.add(false);
            }
        }
        return newInformation;
    }

    /**
     * Converts the analogical information coded in NRZT (None Return to Zero trapeze) into logical information
     *
     * @param information (Information<Float>) Logical Information that contains the data
     * @return Information<Boolean>
     */
    private Information<Boolean> nrztToLogic(Information<Float> information) {
        // Construct the new Information<Boolean>
        Information<Boolean> newInformation = new Information<>();
        // Iterate through the variable Information<Float>
        for (int i = 0; i < nbElements; i = i + nbEchTpsBit) {
            float mean = 0.00f;
            for (int n = nbEchTpsBit / 3; n < 2 * nbEchTpsBit / 3; n++) {
                mean += information.iemeElement(i + n);
            }
            mean = mean / (nbEchTpsBit / 3.00f);
            // Quantify
            if (compareValues(seuilMin, seuilMax, mean) == seuilMax) {
                newInformation.add(true);
            } else {
                newInformation.add(false);
            }
        }
        return newInformation;

    }

    /**
     * Converts the analogical information coded in RZ (Return to Zero) into logical information
     *
     * @param information (Information<Float>) Logical Information that contains the data
     * @return Information<Boolean>
     */
    private Information<Boolean> rzToLogic(Information<Float> information) {
        // Construct the new Information<Boolean>
        Information<Boolean> newInformation = new Information<>();
        float mean;
        // Iterate through the variable Information<Float>
        for (int i = 0; i < nbElements; i = i + nbEchTpsBit) {
            mean = 0.00f;
            for (int n = nbEchTpsBit / 3; n < 2 * nbEchTpsBit / 3; n++) {
                mean += information.iemeElement(i + n);
            }
            mean = mean / (nbEchTpsBit / 3.00f);
            // Quantify
            if (compareValues(seuilMin, seuilMax, mean) == seuilMax) {
                newInformation.add(true);
            } else {
                newInformation.add(false);
            }
        }

        return newInformation;

    }

    /**
     * Compare a float to 2 others float and returns the closest float
     *
     * @param valueA         (float) One of the two value
     * @param valueB         (float) One of the two value
     * @param valueToCompare (float) Value to be compare
     * @return The closest value
     */
    private float compareValues(float valueA, float valueB, float valueToCompare) {
        if (Math.abs(valueToCompare - valueA) < Math.abs(valueToCompare - valueB)) {
            return valueA;
        } else {
            return valueB;
        }
    }

    /**
     * Corrects the multi paths generated in the transmitter
     *
     * @param information (Information<Float>) Information to be corrected
     * @return Information<Float>
     */
    private Information<Float> fixMultiPaths(Information<Float> information) {
        System.out.println("USE OF MP");
        Information<Float> newInformation = new Information<>();
        for (int i = 0; i < nbElements; i++) {
            for (int k = 0; k < dt.size(); k++) {
                if (i >= newInformation.nbElements()) {
                    if (i - dt.get(k) < 0) {
                        newInformation.add(information.iemeElement(i));
                    } else {
                        newInformation.add(information.iemeElement(i) - ar.get(k) * newInformation.iemeElement(i - dt.get(k)));
                    }
                } else if (i - dt.get(k) >= 0) {
                    newInformation.setIemeElement(i,
                            newInformation.iemeElement(i) - ar.get(k) * newInformation.iemeElement(i - dt.get(k)));

                }
            }
        }
        return newInformation;
    }
}
