package MachineNilNovi;

public class Lecteur {
    private String fichier;
    private int ligne;

    public Lecteur(String fichier){
        this.fichier = fichier;
        this.ligne = 1;
    }

    public String getLigne (){

    }

    public void next(){
        ligne = ligne + 1
    }

    public void go(int ligne){
        this.ligne = ligne;
    }
}
