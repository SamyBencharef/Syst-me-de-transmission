import emetteurs.Emetteur;
import information.Information;
import recepteurs.Recepteur;
import sources.*;
import destinations.*;
import transmetteurs.*;
import visualisations.*;


/**
 * La classe Simulateur permet de construire et simuler une chaîne de transmission composée d'une Source, d'un nombre
 * variable de Transmetteur(s) et d'une Destination.
 *
 * @author cousin
 * @author prou
 * @author Thierry JIAO - Samy BENCHAREF - Thanh le HUY - Milo THIBAUD - Lucas BERENGUER
 */
public class Simulateur {

    /**
     * indique si la version du TP à utiliser
     */
    private Integer TP = 3;
    /**
     * indique si le Simulateur utilise des sondes d'affichage
     */
    private boolean affichage = false;
    /**
     * indique si le Simulateur utilise un message généré de manière aléatoire
     */
    private boolean messageAleatoire = true;
    /**
     * indique si le Simulateur utilise un germe pour initialiser les générateurs aléatoires
     */
    private boolean aleatoireAvecGerme = false;
    /**
     * la valeur de la semence utilisée pour les générateurs aléatoires
     */
    private Integer seed = null;
    /**
     * la longueur du message aléatoire à transmettre si un message n'est pas impose
     */
    private int nbBitsMess = 100;
    /**
     * la chaîne de caractères correspondant à m dans l'argument -mess m
     */
    private String messageString = "100";
    /**
     * la chaîne de la forme d'onde pour une transmission analogique TP 2
     */
    private String waveForm = "RZ";
    /**
     * la valeur du nombre d’échantillons par bit pour un echantillonnage si elle n'est pas imposee TP 2
     */
    private Integer ne = 30;
    /**
     * la valeur min de l'amplitude du signal TP 2
     */
    private Float ampliMin = 0.00f;
    /**
     * la valeur max de l'amplitude du signal TP 2
     */
    private Float ampliMax = 1.00f;
    /**
     * la valeur du rapport signal sur bruit par bit (en dB) TP 3
     */
    private Float snrpb = 0.00f;
    /**
     * indique si la densité de probabilité du bruit doit être affichée TP 3
     */
    private boolean hist = false;


    /**
     * le  composant Source de la chaine de transmission
     */
    private Source<Boolean> source = null;
    /**
     * le  composant Transmetteur parfait logique de la chaine de transmission
     */
    private Transmetteur<Boolean, Boolean> transmetteurLogique = null;
    /**
     * le  composant Destination de la chaine de transmission
     */
    private Destination<Boolean> destination = null;


    /**
     * Le constructeur de Simulateur construit une chaîne de transmission composée d'une Source <Boolean>, d'une
     * Destination
     * <Boolean> et de Transmetteur(s) [voir la méthode
     * analyseArguments]...  <br> Les différents composants de la chaîne de transmission (Source, Transmetteur(s),
     * Destination, Sonde(s) de visualisation) sont créés et connectés. Dans notre simulateur, tous les composants
     * connaissent l'horloge.
     *
     * @param args le tableau des différents arguments.
     * @throws ArgumentsException si un des arguments est incorrect
     */
    public Simulateur(String[] args) throws ArgumentsException {

        // Set the arguments given by the user
        analyseArguments(args);

        // Run the version of the TP
        switch (TP) {
            case 1:
                TP1();
                break;
            case 2:
                TP2();
                break;
            case 3:
                TP3();
                break;
        }
    }


    /**
     * La méthode analyseArguments extrait d'un tableau de chaînes de caractères les différentes options de la
     * simulation.  Elle met à jour les attributs du Simulateur.
     *
     * @param args le tableau des différents arguments.
     *             <br>
     *             <br>Les arguments autorisés sont :
     *             <br>
     *             <dl>
     *             <dt> -mess m  </dt><dd> m (String) constitué de 7 ou plus digits à 0 | 1, le message à transmettre</dd>
     *             <dt> -mess m  </dt><dd> m (int) constitué de 1 à 6 digits, le nombre de bits du message "aléatoire" à  transmettre</dd>
     *             <dt> -s </dt><dd> utilisation des sondes d'affichage</dd>
     *             <dt> -seed v </dt><dd> v (int) d'initialisation pour les générateurs aléatoires</dd>
     *             </dl>
     * @throws ArgumentsException si un des arguments est incorrect.
     */
    public void analyseArguments(String[] args) throws ArgumentsException {

        for (int i = 0; i < args.length; i++) {

            if (args[i].matches("-s")) {
                affichage = true;
            } else if (args[i].matches("-seed")) {
                aleatoireAvecGerme = true;
                i++;
                // traiter la valeur associee
                try {
                    seed = Integer.valueOf(args[i]);
                } catch (Exception e) {
                    throw new ArgumentsException("Valeur du parametre -seed  invalide :" + args[i]);
                }
            } else if (args[i].matches("-mess")) {
                i++;
                // traiter la valeur associee
                messageString = args[i];
                if (args[i].matches("[0,1]{7,}")) {
                    messageAleatoire = false;
                    nbBitsMess = args[i].length();
                } else if (args[i].matches("[0-9]{1,6}")) {
                    messageAleatoire = true;
                    nbBitsMess = Integer.valueOf(args[i]);
                    if (nbBitsMess < 1) {
                        throw new ArgumentsException("Valeur du parametre -mess invalide : " + nbBitsMess);
                    }
                } else {
                    throw new ArgumentsException("Valeur du parametre -mess invalide : " + args[i]);
                }
            } else if (args[i].matches("-form")) {
                i++;
                // Treatment
                if (args[i].matches("NRZ")) {
                    waveForm = "NRZ";
                } else if (args[i].matches("NRZT")) {
                    waveForm = "NRZT";
                } else if (args[i].matches("RZ")) {
                    waveForm = "RZ";
                } else {
                    throw new ArgumentsException("Valeur du parametre -form invalide : " + args[i]);
                }
            } else if (args[i].matches("-nbEch")) {
                i++;
                // Treatment
                if (args[i].matches("^[1-9]\\d*$")) {
                    ne = Integer.valueOf(args[i]);
                } else {
                    throw new ArgumentsException("Valeur du parametre -nbEch invalide : " + args[i]);
                }
            } else if (args[i].matches("-ampl")) {
                i++;
                // Treatment
                if (args[i].matches("[+-]?([0-9]*[.])?[0-9]+") && args[i + 1].matches("[+-]?([0-9]*[.])?[0-9]+")) {
                    ampliMin = Float.valueOf(args[i]);
                    i++;
                    ampliMax = Float.valueOf(args[i]);
                } else {
                    throw new ArgumentsException("Valeur du parametre -ampl invalide : " + args[i]);
                }
                if (ampliMin >= ampliMax || ampliMin > 0 || ampliMax < 0) {
                    throw new ArgumentsException("Valeur du parametre -ampl invalide : " + args[i]);
                }
            } else if (args[i].matches("-snrpb")) {
                i++;
                // Treatment
                if (args[i].matches("[+-]?([0-9]*[.])?[0-9]+")) {
                    snrpb = Float.valueOf(args[i]);
                } else {
                    throw new ArgumentsException("Valeur du parametre -snrpb invalide : " + args[i]);
                }
            } else if (args[i].matches("-hist")) {
                hist = true;
            } else if (args[i].matches("-TP")) {
                i++;
                // Treatment
                if (args[i].matches("[1-3]")) {
                    TP = Integer.valueOf(args[i]);
                } else {
                    throw new ArgumentsException("Valeur du parametre -TP invalide : " + args[i]);
                }
            } else {
                throw new ArgumentsException("Option invalide :" + args[i]);
            }
        }

        // Check if the args(-ampl)
        if (ampliMin != 0 && waveForm.equals("RZ")) {
            throw new ArgumentsException("Valeur du parametre -ampl invalide : " + ampliMin);
        }

    }


    /**
     * La méthode execute effectue un envoi de message par la source de la chaîne de transmission du Simulateur.
     *
     * @throws Exception si un problème survient lors de l'exécution
     */
    public void execute() throws Exception {

        source.emettre();

    }


    /**
     * La méthode qui calcule le taux d'erreur binaire en comparant les bits du message émis avec ceux du message reçu.
     *
     * @return La valeur du Taux dErreur Binaire.
     */
    public float calculTauxErreurBinaire() {

        // Get the send message and the received message
        Information<Boolean> sendMessage = source.getInformationEmise();
        Information<Boolean> receivedMessage = destination.getInformationRecue();

        // Throws an Exception if the messages don't have the same length
        if ((source.getInformationEmise().nbElements() != nbBitsMess) || (destination.getInformationRecue().nbElements() != nbBitsMess)) {
            throw new IllegalArgumentException("The length of the send message isn't equal to the received one");
        }

        int bitError = 0;
        int nbBits = source.getInformationEmise().nbElements();

        // Compare char per char
        for (int i = 0; i < nbBits; i++) {
            if (!sendMessage.iemeElement(i).equals(receivedMessage.iemeElement(i))) bitError++;
        }

        return (float) bitError / (float) nbBits;
    }

    private void TP1() {
        // Instantiations of source, transmitter and destination
        // If the message is random and the seed is given
        if (messageAleatoire && aleatoireAvecGerme) {
            source = new SourceAleatoire(nbBitsMess, seed);
        }
        // If the message is random
        else if (messageAleatoire) {
            source = new SourceAleatoire(nbBitsMess);
        }
        // If the message was given by the user
        else {
            source = new SourceFixe(messageString);
        }
        transmetteurLogique = new TransmetteurParfaitLogique();
        destination = new DestinationFinale();

        // Connections between components
        source.connecter(transmetteurLogique);
        transmetteurLogique.connecter(destination);

        // Display graphics
        if (affichage) {
            SondeLogique sonde1 = new SondeLogique("SEND MESSAGE", 100);
            SondeLogique sonde2 = new SondeLogique("RECEIVED MESSAGE", 100);
            source.connecter(sonde1);
            transmetteurLogique.connecter(sonde2);
        }
    }

    private void TP2() {

        // Instantiations of source, transmitter, recepteur, emetteur and destination
        // If the message is random and the seed is given
        if (messageAleatoire && aleatoireAvecGerme) {
            source = new SourceAleatoire(nbBitsMess, seed);
        }
        // If the message is random
        else if (messageAleatoire) {
            source = new SourceAleatoire(nbBitsMess);
        }
        // If the message was given by the user
        else {
            source = new SourceFixe(messageString);
        }
        Emetteur emetteur = new Emetteur(waveForm, ne, ampliMax, ampliMin);
        Recepteur recepteur = new Recepteur(waveForm, ne, ampliMax, ampliMin);
        TransmetteurParfaitAnalogique transmetteurAnalogique = new TransmetteurParfaitAnalogique();
        destination = new DestinationFinale();

        // Connections between components
        source.connecter(emetteur);
        emetteur.connecter(transmetteurAnalogique);
        transmetteurAnalogique.connecter(recepteur);
        recepteur.connecter(destination);

        // Display graphics
        if (affichage) {
            SondeLogique sonde1 = new SondeLogique("SEND MESSAGE LOGICAL", 100);
            SondeAnalogique sonde2 = new SondeAnalogique("SEND MESSAGE ANALOGICAL");
            SondeAnalogique sonde3 = new SondeAnalogique("RECEIVED MESSAGE ANALOGICAL");
            SondeLogique sonde4 = new SondeLogique("RECEVEID MESSAGE LOGICAL", 100);
            source.connecter(sonde1);
            emetteur.connecter(sonde2);
            transmetteurAnalogique.connecter(sonde3);
            recepteur.connecter(sonde4);
        }
    }

    private void TP3() {
        // Instantiations of source, transmitter, recepteur, emetteur and destination
        // If the message is random and the seed is given
        if (messageAleatoire && aleatoireAvecGerme) {
            source = new SourceAleatoire(nbBitsMess, seed);
        }
        // If the message is random
        else if (messageAleatoire) {
            source = new SourceAleatoire(nbBitsMess);
        }
        // If the message was given by the user
        else {
            source = new SourceFixe(messageString);
        }
        Emetteur emetteur = new Emetteur(waveForm, ne, ampliMax, ampliMin);
        Recepteur recepteur = new Recepteur(waveForm, ne, ampliMax, ampliMin);
        TransmetteurBruiteAnalogique transmetteurAnalogique = new TransmetteurBruiteAnalogique(snrpb, ne, hist);
        if (seed != null) {
            transmetteurAnalogique =
                    new TransmetteurBruiteAnalogique(snrpb, ne, hist, seed);
        }
        destination = new DestinationFinale();

        // Connections between components
        source.connecter(emetteur);
        emetteur.connecter(transmetteurAnalogique);
        transmetteurAnalogique.connecter(recepteur);
        recepteur.connecter(destination);

        // Display graphics
        if (affichage) {
            SondeLogique sonde1 = new SondeLogique("SEND MESSAGE LOGICAL", 100);
            SondeAnalogique sonde2 = new SondeAnalogique("SEND MESSAGE ANALOGICAL");
            SondeAnalogique sonde3 = new SondeAnalogique("RECEIVED MESSAGE ANALOGICAL");
            SondeLogique sonde4 = new SondeLogique("RECEVEID MESSAGE LOGICAL", 100);
            source.connecter(sonde1);
            emetteur.connecter(sonde2);
            transmetteurAnalogique.connecter(sonde3);
            recepteur.connecter(sonde4);
        }
    }

    /**
     * La fonction main instancie un Simulateur à l'aide des arguments paramètres et affiche le résultat de l'exécution
     * d'une transmission.
     *
     * @param args les différents arguments qui serviront à l'instanciation du Simulateur.
     */
    public static void main(String[] args) {

        Simulateur simulateur = null;

        try {
            simulateur = new Simulateur(args);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }

        try {
            simulateur.execute();
            float tauxErreurBinaire = simulateur.calculTauxErreurBinaire();
            String s = "java  Simulateur  ";
            for (String arg : args) {
                s += arg + "  ";
            }
            System.out.println(s + "  =>   TEB : " + tauxErreurBinaire);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(-2);
        }
    }
}


