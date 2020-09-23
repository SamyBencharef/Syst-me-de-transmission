import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SimulateurTest{

    public void test(String[] args) throws Exception {
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

    @Test
    public void testSimulateurEchec(){
        try{
            test(new String[]{ "-mess", "1000", "-form", "RZ","-nbEch","20" ,"-ampl", "0.0", "1.0",
                    "-snrpb", "-500"});
        }catch (Exception e){
            fail(e);
        }
    }

    @Test
    public void testSimulateur() {
        String [] args = { "-mess", "101110011", "-form", "NRZT", "-ampl", "-5", "5",
                "-nbEch", "600", "-TP", "2"};
        try{
            Simulateur.main(args);
        }catch (Exception e){
            fail(e);
        }
    }
}