package sources;

import information.Information;

/**
 * Class that defines the component source and that emits specified information defined by the user. The outype type is
 * a Boolean. The class implements the class Source.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class SourceFixe extends Source<Boolean> {

    public SourceFixe(String messageString) {

        // Convert messageString to Boolean[]
        Boolean[] messageBooleans = new Boolean[messageString.length()];
        for (int i = 0; i < messageString.length(); i++) {
            char letter = messageString.charAt(i);
            if (letter == '1') {
                messageBooleans[i] = true;
            } else if (letter == '0') {
                messageBooleans[i] = false;
            } else {
                throw new IllegalArgumentException("Character doesn't have the values 0 or 1");
            }
        }

        // Set the attribute informationGeneree
        this.informationGeneree = new Information<>(messageBooleans);
    }
}
