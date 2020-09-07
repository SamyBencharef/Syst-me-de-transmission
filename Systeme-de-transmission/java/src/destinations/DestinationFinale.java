package destinations;

import information.Information;
import information.InformationNonConforme;

/**
 * Classe Abstraite d'un composant destination d'informations dont les
 * éléments sont de type T
 *
 * @author prou
 */
public class DestinationFinale extends Destination<Boolean> {

    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        this.informationRecue = information;
    }
}
