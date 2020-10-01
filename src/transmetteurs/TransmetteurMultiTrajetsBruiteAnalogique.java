package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import visualisations.Histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class that defines a imperfect analogical transmitter (receive and send the same message by adding noise and
 * multi-paths). The noise generated is gaussian. The input type is a Float and the output type is a Float. The class
 * implements the class Transmetteur.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class TransmetteurMultiTrajetsBruiteAnalogique extends Transmetteur<Float, Float> {

    private final ArrayList<Integer> dt;
    private final ArrayList<Float> ar;
    private final Integer seed;
    private final Integer nbEchTpsBit;
    private final boolean histogram;
    private final Float snrpb;

    /**
     * The constructor sets the attributes of the class TransmetteurMultiTrajetsBruiteAnalogique.
     *
     * @param snrpb       (Float) SNRPB of the signal
     * @param nbEchTpsBit (Integer) NNumber of samples for one bit
     * @param dt          (ArrayList<Integer>) Delay of the indirect Signals
     * @param hist        (boolean) Display or not the Gaussian noise
     * @param ar          (ArrayList<Float>) Attenuation of the indirect Signals amplitude
     */
    public TransmetteurMultiTrajetsBruiteAnalogique(Integer nbEchTpsBit, Float snrpb, ArrayList<Integer> dt,
                                                    ArrayList<Float> ar, boolean hist) {
        super();
        this.nbEchTpsBit = nbEchTpsBit;
        this.snrpb = snrpb;
        this.dt = (ArrayList<Integer>) dt.clone();
        this.ar = (ArrayList<Float>) ar.clone();
        this.histogram = hist;
        this.seed = null;
    }

    /**
     * The constructor sets the attributes of the class TransmetteurMultiTrajetsBruiteAnalogique.
     *
     * @param snrpb       (Float) SNRPB of the signal
     * @param nbEchTpsBit (Integer) NNumber of samples for one bit
     * @param dt          (ArrayList<Integer>) Delay of the indirect Signals
     * @param ar          (ArrayList<Float>) Attenuation of the indirect Signals amplitude
     * @param hist        (boolean) Display or not the Gaussian noise
     * @param seed        (int) Set the seed for the random
     */
    public TransmetteurMultiTrajetsBruiteAnalogique(Integer nbEchTpsBit, Float snrpb, ArrayList<Integer> dt,
                                                    ArrayList<Float> ar, int seed, boolean hist) {
        super();
        this.nbEchTpsBit = nbEchTpsBit;
        this.snrpb = snrpb;
        this.dt = (ArrayList<Integer>) dt.clone();
        this.ar = (ArrayList<Float>) ar.clone();
        this.histogram = hist;
        this.seed = seed;
    }

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
        this.informationRecue = information;
        emettre();
    }

    @Override
    public void emettre() throws InformationNonConforme {
        // Add multi-paths to the information
        if (!(dt.size() == 1 && dt.contains(0))) {
            this.informationEmise = addNoise(addMultiPaths(this.informationRecue, dt, ar));
        } else {
            this.informationEmise = addNoise(informationRecue);
        }
        // Send the message to the others destinations
        for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(this.informationEmise);
        }
    }

    /**
     * Adds multi-paths to a signal.
     *
     * @param signal (Information<Float>) Origginal signal
     * @param dt     (ArrayList<Integer>) List containing the delays of the indirect signals
     * @param ar     (ArrayList<Float>) List containing the amplitude of the indirect signals
     */
    private Information<Float> addMultiPaths(Information<Float> signal, ArrayList<Integer> dt, ArrayList<Float> ar) {
        Information<Float> mixedSignal = new Information<>();
        // Get the index of the largest indirect signal
        int maxSizeIndex = dt.indexOf(Collections.max(dt));
        // Copy the original signal
        mixedSignal.setContent(signal.getContent());
        // Add the delay to the signal
        for (int j = 0; j < dt.get(maxSizeIndex); j++) {
            mixedSignal.add(0, 0f);
        }
        // Attenuate the indirect signal and add the original signal
        for (int i = 0; i < mixedSignal.nbElements(); i++) {
            if (i < signal.nbElements()) {
                mixedSignal.set(i, ar.get(maxSizeIndex) * mixedSignal.iemeElement(i) + signal.iemeElement(i));
            } else {
                mixedSignal.set(i, ar.get(maxSizeIndex) * mixedSignal.iemeElement(i));
            }
        }
        Information<Float> indirectSignal = new Information<>();
        // Iterate through the others indirect signals and add them to the mixed signal
        for (int i = 0; i < dt.size(); i++) {
            if (i != maxSizeIndex) {
                indirectSignal.setContent(signal.getContent());
                // Add delay at the beginning of the signal
                for (int j = 0; j < dt.get(i); j++) {
                    indirectSignal.add(0, 0f);
                }
                for (int j = 0; j < indirectSignal.nbElements(); j++) {
                    mixedSignal.set(j, ar.get(i) * indirectSignal.iemeElement(j) + mixedSignal.iemeElement(j));
                }
            }
        }
        return mixedSignal;
    }

    /**
     * Adds a gaussian noise in the information
     *
     * @param information (Information<Float>) Initial information
     * @return noisy information.
     */
    private Information<Float> addNoise(Information<Float> information) {
        Information<Float> noisyInformation = new Information<>();
        if (snrpb != null) {
            ArrayList<Float> arrayNoise = new ArrayList<>();
            Random ran = new Random();
            if (seed != null) {
                ran = new Random(seed);
            }
            float standardDeviation = (float) Math.sqrt((getSignalPower(information) * nbEchTpsBit) / (2 * Math.pow(10,
                    snrpb / 10)));
            // Mix the noise and information
            for (int i = 0; i < information.nbElements(); i++) {
                float noise =
                        (float) (standardDeviation * Math.sqrt(-2 * Math.log(1 - ran.nextFloat())) * Math.cos(2 * Math.PI * ran.nextFloat()));
                arrayNoise.add(noise);
                noisyInformation.add(noise + information.iemeElement(i));
            }
            if (histogram) showNoise(arrayNoise);
            float noise = 0f;
            for (Float aFloat : arrayNoise) {
                noise += Math.pow(aFloat, 2);
            }
            noise = noise / arrayNoise.size();
            System.out.println("Calculated snrpb " + 10 * Math.log10((getSignalPower(information) * nbEchTpsBit) / (2 * noise)));
        } else {
            noisyInformation = information;
        }
        return noisyInformation;
    }

    /**
     * Calculates the power of a signal and returns it.
     *
     * @param information (Information<Float>) Information
     * @return the power of the signal
     */
    private float getSignalPower(Information<Float> information) {
        float signalPower = 0.00f;
        if (information.nbElements() != 0) {
            for (int i = 0; i < information.nbElements(); i++) {
                signalPower += Math.pow(information.iemeElement(i), 2);
            }
            signalPower = signalPower / information.nbElements();
        }
        return signalPower;
    }

    /**
     * Displays the gaussian noise as a density probability
     *
     * @param noise (ArrayList<Float) noise
     */
    private void showNoise(ArrayList<Float> noise) {
        // Format the value to x.xx
        for (int i = 0; i < noise.size(); i++) {
            noise.set(i, (float) (Math.floor(noise.get(i) * 100) / 100));
        }
        // Sort the values
        Collections.sort(noise);
        // Display
        new Histogram(noise, "Noise");
    }
}
