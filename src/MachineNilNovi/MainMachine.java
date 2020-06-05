package MachineNilNovi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainMachine {

    public static void main(String[] args) throws IOException {
        MachineNilNovi m = new MachineNilNovi();
        m.compilation("codeObjets\\text1.txt");
    }
}
