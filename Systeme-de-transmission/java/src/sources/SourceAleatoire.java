package sources;

import information.Information;

import java.util.Random;

/**
 * Classe Abstraite d'un composant source d'informations dont les
 * éléments sont de type T
 *
 * @author prou
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

