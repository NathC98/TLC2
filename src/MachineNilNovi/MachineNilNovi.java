package MachineNilNovi;

public class MachineNilNovi {
    private Lecteur lecteur;
    private int ip;
    private ArrayList<Integer> maPile
    private Arraylist<Object> TableIdentificateur;
    private boolean fin;

    public MachineNilNovi(ArrayList<Object> ids, String fichier){
        this.TableIdentificateur = ids;
        this.lecteur = new Lecteur(fichier);
    }

    public void debutProg(){
        maPile = new Stack<Integer>();
        registres = new ArrayList<>();
        this.fin = false;
    }

    public void finProg(){
        this.fin = true;
    }

    public void reserver(int n){
        if (!fin){
            if (n >= 0) ip = n-1;
        }
    }

    public void empiler(int val){
        if (!fin){
            maPile[ip] = val;
            ip++;
        }
    }

    public void affectation (){
        if (!fin){
        }
    }

    public void valeurPile(){
        if (!fin){
        }
    }

    public void get(){
        if (!fin){
        }
    }

    public void put(){
        if (!fin){
        }
    }

    public void moins(){
        if (!fin){
        }
    }

    public void sous(){
        if (!fin){
        }
    }

    public void add(){
        if (!fin){
        }
    }

    public void mult(){
        if (!fin){
        }
    }

    public void div(){
        if (!fin){
        }
    }

    public void equal(){
        if (!fin){
        }
    }

    public void diff(){
        if (!fin){
        }
    }

    public void inf(){
        if (!fin){
        }
    }

    public void infeg(){
        if (!fin){
        }
    }

    public void sup(){
        if (!fin){
        }
    }

    public void supeg(){
        if (!fin){
        }
    }

    public void et(){
        if (!fin){
        }
    }

    public void ou(){
        if (!fin){
        }
    }

    public void non(){
        if (!fin){
        }
    }

    public void tra(int ad){
        if (!fin){
        }
    }

    public void tze(int ad){
        if (!fin){
        }
    }

    public void erreur(){
        if (!fin){
        }
    }

}