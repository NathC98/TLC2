package MachineNilNovi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Lecteur {
    private String fichier;
    private int co;
    private ArrayList<String> file;

    public Lecteur(String fichier) throws IOException {
        this.fichier = fichier;
        this.co = 1;
        BufferedReader lecteurAvecBuffer = null;
        String ligne;

        try
        {
            lecteurAvecBuffer = new BufferedReader(new FileReader(fichier));
        }
        catch(FileNotFoundException exc)
        {
            System.out.println("Erreur d'ouverture");
        }
        while ((ligne = lecteurAvecBuffer.readLine()) != null)
            file.add(ligne);
        lecteurAvecBuffer.close();
    }


    public int getCo(){
        return co;
    }

    public String readLigne() {
        String s = getLigne();
        return s;
    }


    public String getLigne (){
        return file.get(co-1);
    }

    public void next(){
        co = co + 1;
    }

    public void go(int ligne){
        this.co = co;
    }

    public static void main(String[] args) throws IOException {
        Lecteur lecteur = new Lecteur("./text1.txt");
        System.out.println(lecteur.getLigne());
    }
}
