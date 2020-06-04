package MachineNilNovi;

public class MachineNilNovi {
    private Lecteur lecteur;
    private int ip;
    private int base;
    private ArrayList<Integer> maPile;
    private ArrayList<Object> TableIdentificateur;
    private boolean fin;


    public void debutProg(){
        maPile = new ArrayList<Integer>();
        maPile.add(0);
        this.fin = false;
        this.ip = 1;
        this.base = 0;
        this.lecteur.next();
    }

    public void finProg(){
        this.fin = true;
        this.lecteur.next();
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
        this.lecteur.next();
    }

    public void empiler(int val){
        if (!fin){
            maPile.add(val);
            ip++;
        }
        this.lecteur.next();
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
        this.lecteur.next();
    }

    public void valeurPile(){
        if (!fin){
            int val = maPile.get(maPile.get(ip));
            maPile.set(ip,val);
        }
        this.lecteur.next();
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
        this.lecteur.next();
    }

    public void put(){
        if (!fin){
            System.out.println(maPile.get(ip));
            maPile.remove(ip);
            ip--;
        }
        this.lecteur.next();
    }

    public void moins(){
        if (!fin){
            maPile.set(ip, -maPile.get(ip));
        }
        this.lecteur.next();
    }

    public void sous(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) - i);
        }
        this.lecteur.next();
    }

    public void add(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) + i);
        }
        this.lecteur.next();
    }

    public void mult(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) * i);
        }
        this.lecteur.next();
    }

    public void div(){
        if (!fin){
            Integer i = maPile.get(ip);
            maPile.remove(ip);
            ip --;
            maPile.set(ip, maPile.get(ip) / i);
        }
        this.lecteur.next();
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
        this.lecteur.next();
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
        this.lecteur.next();
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
        this.lecteur.next();
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
        this.lecteur.next();
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
        this.lecteur.next();
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
        this.lecteur.next();
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
        this.lecteur.next();
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
        this.lecteur.next();
    }

    public void non(){
        if (!fin){
            Integer i = maPile.get(ip);
            if (i == 0)maPile.set(ip, 1);
            if (i == 1)maPile.set(ip, 0);
        }
        this.lecteur.next();
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
            maPile.add(ip, base + ad + 2);
        }
        this.lecteur.next();
    }

    public void reserverBloc(){
        if (!fin){
            ip++;
            maPile.add(ip, base);
            ip++;
            maPile.add(0);
        }
        this.lecteur.next();
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
            maPile.add(maPile.get(base + 2 + ad));
            ip++;
        }
        this.lecteur.next();
    }

    public void parse(String s){
        String sligne = lecteur.getLigne();
        String nomFonct;
        String param;
        int s = 0;
        while(sligne.charAt(s) != '('){
            s++;
        }
        nomFonct =sligne.substring(0,s);
        param = sligne.substring(s);

        if (nomFonct.compareTo("debutProg") == 0){
            this.debutProg();
        }
        if (nomFonct.compareTo("finProg") == 0){
            this.finProg();
        }
        if (nomFonct.compareTo("reserver") == 0){
            int ad = 0;
            int i = 0;
            while(param.charAt(i) != ')'){
                i++;
            }
            String param1 = param.substring(1,i);
            ad = Integer.parseInt(param1);
            this.reserver(ad);
        }
        if (nomFonct.compareTo("empiler") == 0){
            int ad = 0;
            int i = 0;
            while(param.charAt(i) != ')'){
                i++;
            }
            String param1 = param.substring(1,i);
            ad = Integer.parseInt(param1);
            this.empiler(ad);
        }
        if (nomFonct.compareTo("empilerAd") == 0){
            int ad = 0;
            int i = 0;
            while(param.charAt(i) != ')'){
                i++;
            }
            String param1 = param.substring(1,i);
            ad = Integer.parseInt(param1);
            this.empilerad(ad);
        }
        if (nomFonct.compareTo("affectation") == 0){
            this.affectation();
        }
        if (nomFonct.compareTo("valeurPile") == 0){
            this.valeurPile();
        }
        if (nomFonct.compareTo("get") == 0){
            this.get();
        }
        if (nomFonct.compareTo("put") == 0){
            this.put();
        }
        if (nomFonct.compareTo("moins") == 0){
            this.moins();
        }
        if (nomFonct.compareTo("sous") == 0){
            this.sous();
        }
        if (nomFonct.compareTo("add") == 0){
            this.add();
        }
        if (nomFonct.compareTo("mult") == 0){
            this.mult();
        }
        if (nomFonct.compareTo("div") == 0){
            this.div();
        }
        if (nomFonct.compareTo("diff") == 0){
            this.diff();
        }
        if (nomFonct.compareTo("inf") == 0){
            this.inf();
        }
        if (nomFonct.compareTo("infeg") == 0){
            this.infeg();
        }
        if (nomFonct.compareTo("sup") == 0){
            this.sup();
        }
        if (nomFonct.compareTo("supeg") == 0){
            this.supeg();
        }
        if (nomFonct.compareTo("et") == 0){
            this.et();
        }
        if (nomFonct.compareTo("ou") == 0){
            this.ou();
        }
        if (nomFonct.compareTo("non") == 0){
            this.non();
        }
        if (nomFonct.compareTo("tra") == 0){
            int ad = 0;
            int i = 0;
            while(param.charAt(i) != ')'){
                i++;
            }
            String param1 = param.substring(1,i);
            ad = Integer.parseInt(param1);
            this.tra(ad);
        }
        if (nomFonct.compareTo("tze") == 0){
            int ad = 0;
            int i = 0;
            while(param.charAt(i) != ')'){
                i++;
            }
            String param1 = param.substring(1,i);
            ad = Integer.parseInt(param1);
            this.tze(ad);
        }
        if (nomFonct.compareTo("reserverBloc") == 0){
            this.reserverBloc();
        }
        if (nomFonct.compareTo("traStat") == 0){
            int a = 0;
            int nbp = 0;
            int i1 = 0;
            while(param.charAt(i1) != ','){
                i1++;
            }
            String param1 = param.substring(1,i1);
            int i2 = i1;
            while(param.charAt(i2) != ')'){
                i2++;
            }
            String param2 = param.substring(i1+1,i2);
            a = Integer.parseInt(param1);
            nbp = Integer.parseInt(param2);
            this.traStat(a,nbp);
        }
        if (nomFonct.compareTo("retourFonct") == 0){
            this.retourFonct();
        }
        if (nomFonct.compareTo("retourProc") == 0){
            this.retourProc();
        }
        if (nomFonct.compareTo("empilerParam") == 0){
            int ad = 0;
            int i = 0;
            while(param.charAt(i) != ')'){
                i++;
            }
            String param1 = param.substring(1,i);
            ad = Integer.parseInt(param1);
            this.empilerParam(ad);
        }
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

    public void compilation(String fichier){
        this.lecteur = new Lecteur(fichier);
        int lenFile = (lecteur.getFile()).length();
        while(lecteur.getCo() != lenFile ){
            this.parse();
        }
    }
}