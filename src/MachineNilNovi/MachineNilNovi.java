package MachineNilNovi;

public class MachineNilNovi {
    private Lecteur lecteur;
    private int ip;
    private int base;
    private ArrayList<Integer> maPile
    private ArrayList<Object> TableIdentificateur;
    private boolean fin;

    public MachineNilNovi(ArrayList<Object> ids, String fichier){
        this.TableIdentificateur = ids;
        this.lecteur = new Lecteur(fichier);
    }

    public void debutProg(){
        maPile = new ArrayList<Integer>();
        maPile.add(0);
        this.fin = false;
        this.ip = 1;
        this.base = 0;
    }

    public void finProg(){
        this.fin = true;
    }

    public void reserver(int n){
        if (!fin){
            if (n >= 0) {
                for (int i = 0; i < n; i++){
                    maPile.add(0);
                }
                ip = ip + n - 1;
            }
        }
    }

    public void empiler(int val){
        if (!fin){
            maPile.add(ip, val);
            ip++;
        }
    }

    public void affectation (){
        if (!fin){
            Integer i = maPile.get(ip);
            Integer j = maPile.get(ip - 1);
            if (j < ip) maPile.set(j, i);
        }
    }

    public void valeurPile(){
        if (!fin){
            int val = maPile.get(maPile.get(ip));
            maPile.set(ip,val);
        }
    }

    public void get(){
        if (!fin){
            Scanner s = new Scanner(System.in);
            System.out.println("Veuillez rentrer une valeur : \n");
            int entrer = s.nextInt();
            maPile.set(maPile.get(ip),entrer);
            maPile.remove(ip);
            ip = ip - 1;

        }
    }

    public void put(){
        if (!fin){
            System.out.println(maPile.get(ip));
            maPile.remove(ip);
            ip--;
        }
    }

    public void moins(){
        if (!fin){
            maPile.set(ip, -maPile.get(ip));
        }
    }

    public void sous(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) - i);
        }
    }

    public void add(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) + i);
        }
    }

    public void mult(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) * i);
        }
    }

    public void div(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) / i);
        }
    }

    public void equal(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            if (maPile.get(ip) == i) maPile.add(1);
            else maPile.add(0);
            ip++;
        }
    }

    public void diff(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            if (maPile.get(ip) != i) maPile.add(1);
            else maPile.add(0);
            ip++;
        }
    }

    public void inf(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            if (maPile.get(ip) < i) maPile.add(1);
            else maPile.add(0);
            ip++;
        }
    }

    public void infeg(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            if (maPile.get(ip) <= i) maPile.add(1);
            else maPile.add(0);
            ip++;
        }
    }

    public void sup(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            if (maPile.get(ip) > i) maPile.add(1);
            else maPile.add(0);
            ip++;
        }
    }

    public void supeg(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            if (maPile.get(ip) >= i) maPile.add(1);
            else maPile.add(0);
            ip++;
        }
    }

    public void et(){
        if (!fin){
            Integer i = maPile.get(ip);
            Integer j = maPile.get(ip - 1);
            if ((i == 0 || i == 1) && (j == 0 || j == 1)) {
                maPile.remove(ip);
                ip--;
                if (j == 1 && i == 1) maPile.add(1);
                else maPile.add(0);
                ip++;
            }
        }
    }

    public void ou(){
        if (!fin){
            Integer i = maPile.get(ip);
            Integer j = maPile.get(ip - 1);
            if ((i == 0 || i == 1) && (j == 0 || j == 1)) {
                maPile.remove(ip);
                ip--;
                if (j == 1 || i == 1) maPile.add(1);
                else maPile.add(0);
                ip++;
            }
        }
    }

    public void non(){
        if (!fin){
            Integer i = maPile.get(ip);
            if (i == 0)maPile.set(ip, 1);
            if (i == 1)maPile.set(ip, 0);
        }
    }

    public void tra(int ad){
        if (!fin){
            ip = ad;
        }
    }

    public void tze(int ad){
        if (!fin){
            if (maPile.get(ip) == 0) ip = ad;
        }
    }

    public void erreur(String s){
        if (!fin){
            System.out.println(s);
        }
    }

    public void empilerad(int ad){
        if (!fin){
            ip++;
            maPile.set(ip,base+ad+2);
        }
    }

    public void reserverBloc(){
        if (!fin){
            ip++;
            maPile.set(ip,base);
            ip++;
        }
    }

    public void traStat(int a, int nbp){
        if (!fin){
            maPile.set(ip-nbp-1,lecteur.getLigne());
            this.base = ip-nbp-2;
            lecteur.go(a);
        }
    }

    public void retourFonction(){
        if (!fin){
            lecteur.go(maPile.get(base+1));
            ip= base;
            base = maPile.get(base);
        }
    }

    public void retourProc(){
        if (!fin){

        }
    }

    public void empilerParam(int ad){
        if (!fin){

        }
    }
    public void parse(String s){

    }

}