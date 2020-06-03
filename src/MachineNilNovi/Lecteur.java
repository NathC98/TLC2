package MachineNilNovi;

public class Lecteur {
    private String fichier;
    private int co;

    public Lecteur(String fichier){
        this.fichier = fichier;
        this.ligne = 1;
    }

    public int getCo(){
        return co;
    }

    public String getLigne (){
        return "";
    }

    public void next(){
        ligne = ligne + 1;
    }

    public void go(int ligne){
        this.ligne = ligne;
    }
}
