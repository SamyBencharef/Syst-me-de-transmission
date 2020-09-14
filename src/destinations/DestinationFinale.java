package destinations;

import information.Information;
import information.InformationNonConforme;

/**
 * Class that defines the component Destination wich elements are Boolean.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class DestinationFinale extends Destination<Boolean> {

    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        this.informationRecue = information;
    }
}
