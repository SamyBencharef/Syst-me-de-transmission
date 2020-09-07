package sources;

import information.Information;

/**
 * Classe Abstraite d'un composant source d'informations dont les
 * éléments sont de type T
 *
 * @author prou
 */
public class SourceFixe extends Source<Boolean> {

    public SourceFixe(Information<Boolean> informationGeneree) {
        this.informationGeneree = informationGeneree;
    }
}
