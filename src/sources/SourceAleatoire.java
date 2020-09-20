package sources;

import information.Information;

import java.util.Random;

/**
 * Class that defines the component source and that emits random information defined by the user. The outype type is a
 * Boolean. The class implements the class Source.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class SourceAleatoire extends Source<Boolean> {
    public SourceAleatoire(int nbBits) {
        // Instantiation of the table of booleans
        Random rd = new Random();
        Boolean[] messageBooleans = new Boolean[nbBits];
        for (int i = 0; i < nbBits; i++) {
            messageBooleans[i] = rd.nextBoolean();
        }
        // Set the attribute informationGeneree
        this.informationGeneree = new Information<>(messageBooleans);
    }

    public SourceAleatoire(int nbBits, int seed) {
        // Instantiation of the table of booleans
        Random rd = new Random(seed);
        Boolean[] messageBooleans = new Boolean[nbBits];
        for (int i = 0; i < nbBits; i++) {
            messageBooleans[i] = rd.nextBoolean();
        }
        // Set the attribute informationGeneree
        this.informationGeneree = new Information<>(messageBooleans);
    }

}

