import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Comparator;

/**
 * Created by Maria on 30.09.2016.
 */
public class Main {
    public static void main(String[] args) {


        try (BufferedReader inputText = new BufferedReader(new FileReader("input_text.txt"))) {

            String s = inputText.readLine();

            new Crack(s);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
