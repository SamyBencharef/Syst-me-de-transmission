package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import visualisations.Histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class that defines a imperfect analogical transmitter (receive and send the same message bu adding noise). The noise
 * generated is gaussian. The input type is a Float and the output type is a Float. The class implements the class
 * Transmetteur.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class TransmetteurBruiteAnalogique extends Transmetteur<Float, Float> {

    private final Float snrpb;
    private final Integer nbEchTpsBit;
    private final boolean histogram;
    private final Integer seed;

    /**
     * The constructor sets the attributes of the class TransmetteurBruiteAnalogique.
     *
     * @param snrpb       (Float) SNRPB of the signal
     * @param nbEchTpsBit (Integer) NNumber of samples for one bit
     * @param hist        (boolean) Display or not the Gaussian noise
     */
    public TransmetteurBruiteAnalogique(Float snrpb, Integer nbEchTpsBit, boolean hist) {
        super();
        this.snrpb = snrpb;
        this.nbEchTpsBit = nbEchTpsBit;
        this.histogram = hist;
        this.seed = null;
    }

    /**
     * The constructor sets the attributes of the class TransmetteurBruiteAnalogique.
     *
     * @param snrpb       (Float) SNRPB of the signal
     * @param nbEchTpsBit (Integer) NNumber of samples for one bit
     * @param hist        (boolean) Display or not the Gaussian noise
     * @param seed        (int) Value of the seed to generate random noise
     */
    public TransmetteurBruiteAnalogique(Float snrpb, Integer nbEchTpsBit, boolean hist, int seed) {
        super();
        this.snrpb = snrpb;
        this.nbEchTpsBit = nbEchTpsBit;
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
        // Add the noise to the information
        this.informationEmise = addNoise(this.informationRecue);
        // Send the message to the others destinations
        for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(this.informationEmise);
        }
    }

    /**
     * Adds a gaussian noise in the information
     *
     * @param information (Information<Float>) Initial information
     * @return noisy information.
     */
    private Information<Float> addNoise(Information<Float> information) {
        Information<Float> noisyInformation;
        if (snrpb != null) {
            ArrayList<Float> arrayNoise = new ArrayList<>();
            Random ran = new Random();
            if (seed != null) {
                ran = new Random(seed);
            }
            float powerSignal = (float) (10 * Math.log(getSignalPower(information)));
            double powerNoise = powerSignal - snrpb;
            powerNoise = Math.pow(10, (powerNoise / 10));
            float stantardDeviatioon = (float) Math.sqrt(powerNoise);
            float a1;
            float a2;
            Float[] signalBruite = new Float[informationRecue.nbElements()];

            for (int i = 0; i < informationRecue.nbElements(); i++) {
                a1 = ran.nextFloat();
                a2 = ran.nextFloat();
                float noise =
                        (float) (stantardDeviatioon * Math.sqrt((-2) * Math.log(1 - a1)) * Math.cos(2 * Math.PI * a2));
                arrayNoise.add(noise);
                signalBruite[i] = noise + informationRecue.iemeElement(i);

            }
            noisyInformation = new Information<>(signalBruite);
            // Display the gaussian noise as a density probability
            if (histogram) showNoise(arrayNoise);
            float noise = 0f;
            for (Float aFloat : arrayNoise) {
                noise += Math.pow(aFloat, 2);
            }
            noise = noise / arrayNoise.size();
//            System.out.println("PuissanceBruit : " + noise);
//            System.out.println("PuissanceSignal : " + (getSignalPower(information)));
//            System.out.println("nbEchTpsBit : " + nbEchTpsBit);
//            System.out.println("Calculated snrpb " + 10 * Math.log10((getSignalPower(information) * nbEchTpsBit) / (2 * noise)));
        } else {
            noisyInformation = new Information<>();
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
