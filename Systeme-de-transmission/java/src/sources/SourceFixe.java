package sources;

import information.Information;

/**
 * Classe Abstraite d'un composant source d'informations dont les
 * éléments sont de type T
 *
 * @author prou
 */
public class SourceFixe extends Source<Boolean> {

    public SourceFixe(String messageString) {

        // Convert messageString to Boolean[]
        Boolean[] messageBooleans = new Boolean[messageString.length()];
        for (int i = 0; i < messageString.length(); i++) {
            char letter = messageString.charAt(i);
            if (letter == '1') messageBooleans[i] = true;
            else if (letter == '0') messageBooleans[i] = false;
            else throw new IllegalArgumentException("Character doesn't have the values 0 or 1");
        }

        // Set the attribute informationGeneree
        this.informationGeneree = new Information<>(messageBooleans);
    }
}
