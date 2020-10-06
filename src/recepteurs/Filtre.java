package recepteurs;

import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;

import java.util.ArrayList;

/**
 *
 */
public class Filtre extends Transmetteur<Float, Float> {
    private final float threshold;
    private final int sizeWindow;

    /**
     * The constructor sets the attributes of the class Filtre.
     *
     * @param nbEchTpsBit (Integer) number of samples per bit
     * @param ampliMax    (Float) Value max of the amplitude from the signal
     * @param ampliMin    (Float) Value min of the amplitude from the signal
     */
    public Filtre(String formeMessage, Integer nbEchTpsBit, Float ampliMax, Float ampliMin) {
        threshold = (ampliMin + ampliMax) / 2;
        if (formeMessage.equals("RZ")) {
            sizeWindow = nbEchTpsBit / 3;
        } else {
            sizeWindow = nbEchTpsBit;
        }
    }

    /**
     * Gets the Float information and stores in his attribute informationRecue. Then, filters the information by using a
     * Hamming window and calls the method emettre().
     *
     * @param information (Information<Float>) Information to filter
     */
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        informationRecue = information;
        informationEmise = filter(padding(informationRecue));
        emettre();
    }

    /**
     * Emits information to all his interfaces in the attribute destinationsConnectees
     */
    public void emettre() throws InformationNonConforme {
        for (destinations.DestinationInterface<Float> destinationsConnectee : destinationsConnectees) {
            destinationsConnectee.recevoir(informationEmise);
        }
    }

    /**
     * Filters the analogical information by using a Hamming Window
     *
     * @param information (Information<Float>) Logical Information that contains the data
     * @return Information<Boolean>
     */
    private Information<Float> filter(Information<Float> information) {
        System.out.println("USE OF FILTER");
        Information<Float> newInformation = new Information<>();
        ArrayList<Float> filtre = hammingWindow(sizeWindow);
        for (int i = sizeWindow / 2; i < information.nbElements() - sizeWindow / 2; i++) {
            float sum = 0f;
            int n = i;
            for (float nFiltre : filtre) {
                sum = sum + nFiltre * information.iemeElement(n + sizeWindow / 2);
                n--;
            }
            newInformation.add(sum);
        }
        return newInformation;
    }

    /**
     * Creates a Hamming window with a defined size.
     *
     * @param sizeWindow (int) Size of the window
     * @return ArrayList<Float>
     */
    private ArrayList<Float> hammingWindow(int sizeWindow) {
        ArrayList<Float> hamming = new ArrayList<>();
        for (int i = 0; i < sizeWindow; i++) {
            hamming.add((float) (2 * (0.53836 - 0.46164 * Math.cos(2 * Math.PI * i / sizeWindow)) / sizeWindow));
        }
        return hamming;
    }

    /**
     * Adds threshold at the beginning of the information and at the end in order to convoluted the information with the
     * Hamming Window.
     *
     * @param information (int) Size of the window
     * @return Information<Float>
     */
    private Information<Float> padding(Information<Float> information) {
        Information<Float> newInformation = new Information<>();
        newInformation.addManyTimes(threshold, sizeWindow / 2);
        for (Float value : information) {
            newInformation.add(value);
        }
        newInformation.addManyTimes(threshold, sizeWindow / 2);
        return newInformation;
    }
}
