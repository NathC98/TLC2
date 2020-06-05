package MachineNilNovi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Lecteur {
    private String fichier;
    private int co;
    private ArrayList<String> file;

    public Lecteur(String fichier) throws IOException {
        this.fichier = fichier;
        this.co = 1;
        BufferedReader lecteurAvecBuffer = null;
        String ligne;
        file = new ArrayList<String>();
        try
        {
            lecteurAvecBuffer = new BufferedReader(new FileReader(fichier));
        }
        catch(FileNotFoundException exc)
        {
            System.out.println("Erreur d'ouverture");
        }
        while ((ligne = lecteurAvecBuffer.readLine()) != null) {
            file.add(ligne);
        }
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

    public ArrayList<String> getFile(){
        return file;
    }

    public void next(){
        co = co + 1;
    }

    public void go(int ligne){
        this.co = co;
    }

    public static void main(String[] args) throws IOException {
        MachineNilNovi m = new MachineNilNovi();
        m.compilation("codeObjets\\text1.txt");
    }
}
