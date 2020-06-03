package MachineNilNovi;

public class MachineNilNovi {
    private Lecteur lecteur;
    private int ip;
    private int base;
    private ArrayList<Integer> maPile;
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
        co++;
    }

    public void finProg(){
        this.fin = true;
        co++;
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
        co++;
    }

    public void empiler(int val){
        if (!fin){
            maPile.add(val);
            ip++;
        }
        co++;
    }

    public void affectation (){
        if (!fin){
            Integer i = maPile.get(ip);
            Integer j = maPile.get(ip - 1);
            if (j < ip) maPile.set(j, i);
            maPile.remove(ip);
            maPile.remove(ip - 1);
            ip = ip - 2;
        }
        co++;
    }

    public void valeurPile(){
        if (!fin){
            int val = maPile.get(maPile.get(ip));
            maPile.set(ip,val);
        }
        co++;
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
        co++;
    }

    public void put(){
        if (!fin){
            System.out.println(maPile.get(ip));
            maPile.remove(ip);
            ip--;
        }
        co++;
    }

    public void moins(){
        if (!fin){
            maPile.set(ip, -maPile.get(ip));
        }
        co++;
    }

    public void sous(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) - i);
        }
        co++;
    }

    public void add(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) + i);
        }
        co++;
    }

    public void mult(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) * i);
        }
        co++;
    }

    public void div(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) / i);
        }
        co++;
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
        co++;
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
        co++;
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
        co++;
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
        co++;
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
        co++;
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
        co++;
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
        co++;
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
        co++;
    }

    public void non(){
        if (!fin){
            Integer i = maPile.get(ip);
            if (i == 0)maPile.set(ip, 1);
            if (i == 1)maPile.set(ip, 0);
        }
        co++;
    }

    public void tra(int ad){
        if (!fin){
            lecteur.go(ad);
        }
    }

    public void tze(int ad){
        if (!fin){
            if (maPile.get(ip) == 0)  lecteur.go(ad);
        }
    }

    public void erreur(String s){
        if (!fin){
            System.out.println(s);
            fin = true;
        }
    }

    public void empilerad(int ad){
        if (!fin){
            ip++;
            maPile.set(ip, base + ad + 2);
        }
        co++;
    }

    public void reserverBloc(){
        if (!fin){
            ip++;
            maPile.add(ip, base);
            ip++;
            maPile.add(0);
        }
        co++;
    }

    public void traStat(int a, int nbp){
        if (!fin){
            maPile.set(ip - nbp - 1, lecteur.getCo());
            this.base = ip - nbp - 2;
            lecteur.go(a);
        }
    }

    public void retourFonction(){
        if (!fin){
            lecteur.go(maPile.get(base+1));
            lecteur.set(base-1,maPile.get(ip));
            temp = base
            base = maPile.get(base);
            while (ip > temp - 1){
                maPile.remove(ip);
                ip--;
            }
        }
    }

    public void retourProc(){
        if (!fin){
            lecteur.go(maPile.get(base + 1));
            temp = base
            base = maPile.get(base);
            while (ip > temp - 2){
                maPile.remove(ip);
                ip--;
            }
        }
    }

    public void empilerParam(int ad){
        if (!fin){
            ip++;
            maPile.set(ip, maPile.get(base + 2 + ad));
        }
        co++;
    }

    public void parse(String s){

    }

    public int getIp() {
        return ip;
    }

    public int getBase() {
        return base;
    }

    public ArrayList<Integer> getMaPile() {
        return maPile;
    }
}