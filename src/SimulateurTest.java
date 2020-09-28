
import org.junit.Before;
import org.junit.Test;
import visualisations.Histogram;
import visualisations.VueCourbe;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Logger;

import static java.lang.Float.parseFloat;
import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;

public class SimulateurTest {

    private static Logger logger;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final String Newligne = System.getProperty("line.separator");
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        logger = Logger.getLogger(String.valueOf(SimulateurTest.class));
    }

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void test(String[] args) throws Exception {
        Simulateur simulateur = null;

        simulateur = new Simulateur(args);

        simulateur.execute();
        float tauxErreurBinaire = simulateur.calculTauxErreurBinaire();
        String s = "java  Simulateur  ";
        for (String arg : args) {
            s += arg + "  ";
        }
        System.out.println(s + "  =>   TEB : " + tauxErreurBinaire);
    }

    // on récupere le TEB avec un bon vieux split et cast des familles
    private double getTEB() {
        String outputFin = outputStreamCaptor.toString().trim();
        String[] splitted = outputFin.split("TEB : ");
        return Double.parseDouble(splitted[1]);
    }

    // ------------------------------------------------- TP1 ------------------------------------------------- \\
    @Test
    public void testTP1_TEB0avecMsg300etSeed() throws Exception {
        logger.info("Test TP1 n°1: Vérifie si TEB  est égale à 0.00. " +
                "Message aléatoire de 300 bits avec un seed donné :");
        test(new String[]{"-mess", "300", "-seed", "90", "-TP", "1"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals("java  Simulateur  -mess  300  -seed  90  -TP  1    =>   TEB : 0.0",
                outputStreamCaptor.toString().trim());
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTP1_TEB0avecMsgDefini() throws Exception {
        logger.info("Test TP1 n°2: Vérifie si TEB  est égale à 0.00 ." +
                "Message défini '101110011'");
        test(new String[]{"-mess", "101110011", "-TP", "1"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals("java  Simulateur  -mess  101110011  -TP  1    =>   TEB : 0.0",
                outputStreamCaptor.toString().trim());
        logger.info("Test OK" + Newligne);
    }

    // ------------------------------------------------- TP2 ------------------------------------------------- \\
    @Test
    public void testTP2_TEB0avecMsgEchNRZTAmplDefini() throws Exception {
        logger.info("Test TP2 n°1: Vérifie si TEB  est égale à 0.00 " +
                "Message défini '101110011', 600 ech par bit, NRZT et d'amplitudes min = -5 max = 5");
        test(new String[]{"-mess", "101110011", "-form", "NRZT", "-ampl", "-5", "5", "-nbEch", "600", "-TP", "2"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals("java  Simulateur  -mess  101110011  -form  NRZT  -ampl  -5  5  -nbEch  600  -TP  2    =>   TEB : 0.0",
                outputStreamCaptor.toString().trim());
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTP2_TEB0avecMsgEchNRZAmplDefini() throws Exception {
        logger.info("Test TP2  n°2: Vérifie si TEB  est égale à 0.00." +
                "Message défini '101110011', 600 ech par bit, NRZ et d'amplitudes min = -5 max = 5");
        test(new String[]{"-mess", "101110011", "-form", "NRZ", "-ampl", "-5", "5", "-nbEch", "600", "-TP", "2"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals("java  Simulateur  -mess  101110011  -form  NRZ  -ampl  -5  5  -nbEch  600  -TP  2    =>   TEB : 0.0",
                outputStreamCaptor.toString().trim());
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTP2_TEB0avecMsgEchRZAmplDefini() throws Exception {
        logger.info("Test TP2 n°3 : Vérifie si TEB  est égale à 0.00." +
                "Message défini '101110011', 600 ech par bit, RZ et d'amplitudes min = 0 max = 5");
        test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "600", "-TP", "2"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals("java  Simulateur  -mess  101110011  -form  RZ  -ampl  0  5  -nbEch  600  -TP  2    =>   TEB : 0.0",
                outputStreamCaptor.toString().trim());
        logger.info("Test OK" + Newligne);
    }

    // ------------------------------------------------- TP3 ------------------------------------------------- \\
    @Test
    public void testTP3_TEB0avecMsgEchNRZTAmplSNRBDefini() throws Exception {
        logger.info("Test TP3 n°1: Vérifie si TEB différent de 0.00." +
                "Message défini '101110011', 600 ech par bit, NRZT et d'amplitudes min = -5 max = 5, snrpb = -5");
        test(new String[]{"-mess", "101110011", "-form", "NRZT", "-ampl", "-5", "5", "-nbEch", "600", "-snrpb", "-5.0", "-TP", "3"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTP3_TEB0avecMsgEchNRZAmplSNRBDefini() throws Exception {
        logger.info("Test TP3 n°2 : Vérifie si TEB différent de 0.00." +
                "Message défini '101110011', 600 ech par bit, NRZ et d'amplitudes min = -5 max = 5");
        test(new String[]{"-mess", "101110011", "-form", "NRZ", "-ampl", "-5", "5", "-nbEch", "600", "-snrpb", "-5.0", "-TP", "3"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0.0, getTEB());
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTP3_TEB0avecMsgEchRZAmplSNRBDefini() throws Exception {
        logger.info("Test TP3 n°3 : Vérifie si TEB différent de 0.00. " +
                "Message défini '101110011', 600 ech par bit, RZ et d'amplitudes min = -5 max = 5");
        test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "600", "-snrpb", "-5.0", "-TP", "3"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0.0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    // ------------------------------------------------- ALL TP's ------------------------------------------------- \\
    // -------- Performance ------ \\
    @Test
    public void testTPs_Performance() throws Exception {

        logger.info("All TP's, Performance n°1 : " +
                "Vérifier la performance (délai -30 secondes). " +
                "Message défini '101110011', 200 ech par bit, RZ et d'amplitudes min = -5 max = 5");

        Date date = new Date();
        long time = date.getTime();
        logger.info("Timestamp de départ en miliseconde: " + time);

        test(new String[]{"-mess", "100000", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0"});
        logger.info(outputStreamCaptor.toString().trim());

        Date date2 = new Date();
        long time2 = date2.getTime();
        logger.info("Timestamp de fin en miliseconde: " + time2);


        if ((time2 - time) > 10000) {
            fail("La performance n'a pas été atteinte.");
        }
        logger.info("Test OK" + Newligne);
    }

    // -------- Bon fontionnement du seed ------ \\
    @Test
    public void testTPs_BonFonctionnementSeed() throws Exception {
        logger.info("All TP's, Seed fonctionnel n°1 : " +
                "Vérifier le bon fonctionnement du seed" +
                "Message défini '101110011', 200 ech par bit, RZ, seed 90, et d'amplitudes min = -5 max = 5");

        test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90"});
        logger.info(outputStreamCaptor.toString().trim());
        double teb1 = getTEB();

        test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90"});
        logger.info(outputStreamCaptor.toString().trim());
        String outputFin = outputStreamCaptor.toString().trim();
        String[] splitted = outputFin.split("TEB : ");
        double teb2 = Double.parseDouble(splitted[2]);

        assertEquals(teb1, teb2);

        logger.info("Test OK" + Newligne);
    }

    // -------- Ouverture des graphiques ------ \\
    @Test
    public void testTPs_AffichageCourbeClassique() throws Exception {
        logger.info("All TP's, Affichage fonctionnel n°1 : " +
                "Vérifier le bon fonctionnement de l'affichage des graphiques" +
                "Message défini '101110011', 40 ech par bit, RZ, seed 90, -s et d'amplitudes min = -5 max = 5");

        test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "5", "-snrpb", "-5.0", "-s"});
        logger.info(outputStreamCaptor.toString().trim());
        Robot robot = new Robot();
        robot.waitForIdle();
        assertNotNull(VueCourbe.class);
        VueCourbe.kill();
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTPs_AffichageHistogramme() throws Exception {
        logger.info("All TP's, Affichage fonctionnel n°1 : " +
                "Vérifier le bon fonctionnement de l'affichage des graphiques" +
                "Message défini '101110011', 40 ech par bit, RZ, seed 90, -hist et d'amplitudes min = -5 max = 5");

        test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "5", "-snrpb", "-5.0", "-hist"});
        logger.info(outputStreamCaptor.toString().trim());
        Robot robot = new Robot();
        robot.waitForIdle();
        assertNotNull(Histogram.class);
        Histogram.kill();
        logger.info("Test OK" + Newligne);
    }

    // -------- Verification d'entrées incorrect ------ \\
    @Test
    public void testTPs_EntreeIncorrect_RZnegatif() throws Exception {
        logger.info("All TP's, entrée incorrect n°1 : " +
                "Vérifier l'entrée de l'amplitude négative d'un RZ. " +
                "Message défini '101110011', 200 ech par bit, RZ, seed 90, et d'amplitudes min = -5 max = 5");
        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "-5", "5", "-nbEch", "200", "-snrpb", "-5.0"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -ampl invalide : -5.0", e.getMessage());
        }
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_EntreeIncorrect_Seed() throws Exception {
        logger.info("All TP's, entrée incorrect n°2 : " +
                "Vérifier l'entrée d'un seed incorrect. " +
                "Message défini '101110011', 200 ech par bit, RZ, seed wrong, et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "wrong"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -seed  invalide :wrong", e.getMessage());
        }
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTPs_EntreeIncorrect_Message1() throws Exception {
        logger.info("All TP's, entrée incorrect n°3 : " +
                "Vérifier l'entrée d'un message incorrect 1. " +
                "Message défini 'wrong', 200 ech par bit, RZ, seed 90, et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "wrong", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -mess invalide : wrong", e.getMessage());
        }
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTPs_EntreeIncorrect_Message2() throws Exception {
        logger.info("All TP's, entrée incorrect n°4 : " +
                "Vérifier l'entrée d'un message incorrect 2. " +
                "Message défini '00000111a1111', 200 ech par bit, RZ, seed 90, et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "00000111a1111", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -mess invalide : 00000111a1111", e.getMessage());
        }
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTPs_EntreeIncorrect_Format() throws Exception {
        logger.info("All TP's, entrée incorrect n°5 : " +
                "Vérifier l'entrée d'un format incorrect. " +
                "Message défini '101110011', 200 ech par bit, format : wrong, seed 90, et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "101110011", "-form", "wrong", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -form invalide : wrong", e.getMessage());
        }
        logger.info("Test OK" + Newligne);
    }

    @Test
    public void testTPs_EntreeIncorrect_nbEch() throws Exception {
        logger.info("All TP's, entrée incorrect n°6 : " +
                "Vérifier l'entrée d'un echantillon incorrect. " +
                "Message défini '101110011', wrong ech par bit, format : RZ, seed 90, et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "wrong", "-snrpb", "-5.0", "-seed", "90"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -nbEch invalide : wrong", e.getMessage());
        }
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_EntreeIncorrect_Amplitude1() throws Exception {
        logger.info("All TP's, entrée incorrect n°7 : " +
                "Vérifier l'entrée d'un amplitude incorrect 1. " +
                "Message défini '101110011', 200 ech par bit, RZ, seed 90, et d'amplitudes min = wrong max = wrong");

        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "wrong", "wrong", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -ampl invalide : wrong", e.getMessage());
        }
        logger.info("Test OK"  + Newligne);
    }

    @Test
    public void testTPs_EntreeIncorrect_Amplitude2() throws Exception {
        logger.info("All TP's, entrée incorrect n°8 : " +
                "Vérifier l'entrée d'un amplitude incorrect 2. " +
                "Message défini '', 200 ech par bit, RZ, seed 90, et d'amplitudes min = -5 max = 5");

        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "5", "0", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -ampl invalide : 0", e.getMessage());
        }
        logger.info("Test OK"  + Newligne);
    }

    @Test
    public void testTPs_EntreeIncorrect_snrpb() throws Exception {
        logger.info("All TP's, entrée incorrect n°9 : " +
                "Vérifier l'entrée d'un snrpb incorrect. " +
                "Message défini '101110011', 200 ech par bit, format : RZ, seed 90, snrpb wrong, et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "wrong", "-seed", "90"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -snrpb invalide : wrong", e.getMessage());
        }
        logger.info("Test OK"  + Newligne);
    }

    @Test
    public void testTPs_EntreeIncorrect_TP() throws Exception {
        logger.info("All TP's, entrée incorrect n°10 : " +
                "Vérifier l'entrée d'un TP incorrect. " +
                "Message défini '101110011', 200 ech par bit, format : RZ, seed 90, TP wrong et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-seed", "90", "-TP", "wrong"});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Valeur du parametre -TP invalide : wrong", e.getMessage());
        }
        logger.info("Test OK"  + Newligne);
    }


    @Test
    public void testTPs_EntreeIncorrect_Option() throws Exception {
        logger.info("All TP's, entrée incorrect n°11 : " +
                "Vérifier l'entrée d'une option incorrect. " +
                "Message défini '101110011', 200 ech par bit, format : RZ, wrong 90 et d'amplitudes min = 0 max = 5");

        try {
            test(new String[]{"-mess", "101110011", "-form", "RZ", "-ampl", "0", "5", "-nbEch", "200", "-snrpb", "-5.0", "-wrong", "90",});
            logger.info(outputStreamCaptor.toString().trim());
            fail("The exception haven't been thrown");
        } catch (ArgumentsException e) {
            logger.info(outputStreamCaptor.toString().trim());
            assertEquals("Option invalide :-wrong", e.getMessage());
        }
        logger.info("Test OK"  + Newligne);
    }

    // -------- Test de plusieurs valeurs, bon fonctionnement  ------ \\
    // -------- RZ ------ \\
    @Test
    public void testTPs_PlusieursEntree_RZ_SNRPBneg500() throws Exception {
        logger.info("All TP's, plusieurs entrée RZ avec snrpb -500: " +
                "Message défini '1000', 20 ech par bit, format : RZ, snrpb -500 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "RZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "-500"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_RZ_SNRPBneg50() throws Exception {
        logger.info("All TP's, plusieurs entrée RZ avec snrpb -50: " +
                "Message défini '1000', 20 ech par bit, format : RZ, snrpb -50 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "RZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "-50"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_RZ_SNRPB0() throws Exception {
        logger.info("All TP's, plusieurs entrée RZ avec snrpb 0: " +
                "Message défini '1000', 20 ech par bit, format : RZ, snrpb 0 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "RZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "0"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_RZ_SNRPB7() throws Exception {
        logger.info("All TP's, plusieurs entrée RZ avec snrpb 7: " +
                "Message défini '1000', 20 ech par bit, format : RZ, snrpb 7 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "RZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "7"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_RZ_SNRPB50() throws Exception {
        logger.info("All TP's, plusieurs entrée RZ avec snrpb 50 : " +
                "Message défini '1000', 20 ech par bit, format : RZ, snrpb 50 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "RZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "50"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals(0.0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    // -------- NRZ ------ \\
    @Test
    public void testTPs_PlusieursEntree_NRZ_SNRPBneg500() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZ avec snrpb -500: " +
                "Message défini '1000', 20 ech par bit, format : NRZ, snrpb -500 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "-500"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZ_SNRPBneg50() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZ avec snrpb -50: " +
                "Message défini '1000', 20 ech par bit, format : NRZ, snrpb -50 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "-50"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZ_SNRPB0() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZ avec snrpb 0: " +
                "Message défini '1000', 20 ech par bit, format : NRZ, snrpb 0 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "0"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZ_SNRPB7() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZ avec snrpb 7: " +
                "Message défini '1000', 20 ech par bit, format : NRZ, snrpb 7 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "7"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZ_SNRPB50() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZ avec snrpb 50 : " +
                "Message défini '1000', 20 ech par bit, format : NRZ, snrpb 50 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZ", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "50"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals(0.0, getTEB());
        logger.info("Test OK" + Newligne);
    }



    // -------- NRZT ------ \\
    @Test
    public void testTPs_PlusieursEntree_NRZT_SNRPBneg500() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZT avec snrpb -500: " +
                "Message défini '1000', 20 ech par bit, format : NRZT, snrpb -500 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZT", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "-500"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZT_SNRPBneg50() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZT avec snrpb -50: " +
                "Message défini '1000', 20 ech par bit, format : NRZT, snrpb -50 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZT", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "-50"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZT_SNRPB0() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZT avec snrpb 0: " +
                "Message défini '1000', 20 ech par bit, format : NRZT, snrpb 0 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZT", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "0"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZT_SNRPB7() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZT avec snrpb 7: " +
                "Message défini '1000', 20 ech par bit, format : NRZT, snrpb 7 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZT", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "7"});
        logger.info(outputStreamCaptor.toString().trim());
        assertNotEquals(0, getTEB());
        logger.info("Test OK" + Newligne);
    }


    @Test
    public void testTPs_PlusieursEntree_NRZT_SNRPB50() throws Exception {
        logger.info("All TP's, plusieurs entrée NRZT avec snrpb 50 : " +
                "Message défini '1000', 20 ech par bit, format : NRZT, snrpb 50 et d'amplitudes min = 0.0 max = 1.0");
        test(new String[]{"-mess", "1000", "-form", "NRZT", "-ampl", "0.0", "1.0", "-nbEch", "10", "-snrpb", "50"});
        logger.info(outputStreamCaptor.toString().trim());
        assertEquals(0.0, getTEB());
        logger.info("Test OK" + Newligne);
    }
}