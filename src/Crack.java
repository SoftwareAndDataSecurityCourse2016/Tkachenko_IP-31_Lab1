import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Maria on 05.10.2016.
 */
public class Crack {
    String input_text;
    Set<String> usedKeys = new HashSet<>();
    TreeMap<Double,Key> results = new TreeMap<>();
    HashMap<String,Double> trigramStatistic = new HashMap<>();
    String[] letters = {"A","B","C","D","E","F","G","H","I","24","K","L","M","N","O","P","Q","R","S","T","U","V","W","23","Y","25" };

    Crack(String input_text) {
        this.input_text = input_text;
        HashMap<String, String> ik = getInitialKey();

        usedKeys.add(stringBuilder(ik));

        try (BufferedReader inputText = new BufferedReader(new FileReader("english_trigrams.txt"))) {

            String s = inputText.readLine();
            while (s != null) {
                String[] str = s.split(" ");
                trigramStatistic.put(str[0], Double.valueOf(str[1]) / 4372870785L);
                s = inputText.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        Key keyInit = new Key(ik, stringBuilder(ik));
        results.put(countStat(ik), keyInit);
        for (int i = 0; i <= 1000; i++) {
            for (HashMap<String, String> key : generateKeys(ik)) {
                Double n = countStat(key);
                results.put(n, new Key(key, stringBuilder(key)));
            }
            ik = results.firstEntry().getValue().oneToOne;
        }
        System.out.println("Критерий Пирсона: "+results.firstEntry().getKey());
        for(Map.Entry lines:results.firstEntry().getValue().oneToOne.entrySet()){
            System.out.println(lines.getKey()+"->"+lines.getValue());
        }
        System.out.println(getCypherText(ik));
    }

    private ArrayList<HashMap<String,String>> generateKeys(HashMap<String,String> initialKey){
        ArrayList<HashMap<String,String>> generatedKeys = new ArrayList<>();
        for(int i=0; i<24;i++){
            HashMap<String,String> initialKeyCopy = (HashMap<String,String>)initialKey.clone();
            String g1 = initialKey.get(letters[i]);
            String g2 = initialKey.get(letters[i+1]);
            initialKeyCopy.replace(letters[i],g2);
            initialKeyCopy.replace(letters[i+1],g1);
            if(noRepeats(stringBuilder(initialKeyCopy)))
            generatedKeys.add(initialKeyCopy);
        }
        Random r = new Random(System.currentTimeMillis());

        for(int i=0; i<500;i++){
            int rand = r.nextInt(25);
            int rand1 = r.nextInt(25);
            HashMap<String,String> initialKeyCopy = (HashMap<String,String>)initialKey.clone();
            String g1 = initialKey.get(letters[rand]);
            String g2 = initialKey.get(letters[rand1]);
            initialKeyCopy.replace(letters[rand],g2);
            initialKeyCopy.replace(letters[rand1],g1);
            if(noRepeats(stringBuilder(initialKeyCopy)))
                generatedKeys.add(initialKeyCopy);

        }
        return generatedKeys;
    }

    private boolean noRepeats(String str){
        String[] letters = str.split("");
        Map<String, Double> stat = new HashMap<String, Double>();
        for(String letter:letters){
            if(!stat.containsKey(letter)){
                stat.put(letter, 1.0 );
            }
            else{
                return false;
            }
        }
        return true;
    }

    private String stringBuilder(HashMap<String,String> key){
        String[] array = key.values().toArray(new String[0]);

        StringBuilder builder = new StringBuilder();
        for(String s : array) {
            builder.append(s);
        }
        return builder.toString();
    }

    private HashMap<String,String> getInitialKey(){
        Map<String, Double> englishStat = new HashMap<String, Double>();
        englishStat.put("A", 0.0651738);
        englishStat.put("B", 0.0124248);
        englishStat.put("C", 0.0217339);
        englishStat.put("D", 0.0349835);
        englishStat.put("E", 0.1041442);
        englishStat.put("F", 0.0197881);
        englishStat.put("G", 0.0158610);
        englishStat.put("H", 0.0492888);
        englishStat.put("I", 0.0558094);
        englishStat.put("J", 0.0009033);
        englishStat.put("K", 0.0050529);
        englishStat.put("L", 0.0331490);
        englishStat.put("M", 0.0202124);
        englishStat.put("N", 0.0564513);
        englishStat.put("O", 0.0596302);
        englishStat.put("P", 0.0137645);
        englishStat.put("Q", 0.0008606);
        englishStat.put("R", 0.0497563);
        englishStat.put("S", 0.0515760);
        englishStat.put("T", 0.0729357);
        englishStat.put("U", 0.0225134);
        englishStat.put("V", 0.0082903);
        englishStat.put("W", 0.0171272);
        englishStat.put("X", 0.0013692);
        englishStat.put("Y", 0.0145984);
        englishStat.put("Z", 0.0007836);


        String[] letters = input_text.split("");
        Map<String, Double> stat = new HashMap<String, Double>();
        for(String letter:letters){
            if(!stat.containsKey(letter)){
                stat.put(letter, 1.0 );
            }
            else{
                Double variable = stat.get(letter);
                variable++;
                stat.replace(letter,variable);
            }
        }

        List list = new ArrayList<>(stat.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object e1, Object e2) {
                return ((Double)((Map.Entry)e2).getValue()).compareTo((Double)((Map.Entry)e1).getValue());
            }
        });


        List englishList = new ArrayList<>(englishStat.entrySet());
        Collections.sort(englishList, new Comparator() {
            @Override
            public int compare(Object e1, Object e2) {
                return ((Double)((Map.Entry)e2).getValue()).compareTo((Double)((Map.Entry)e1).getValue());
            }
        });

        HashMap<String, String> key = new HashMap<String, String>();
        for(int i=0;i<englishList.size();i++){
            if(i>=list.size()) key.put(String.valueOf(i),((Map.Entry)englishList.get(i)).getKey().toString());
            else key.put(((Map.Entry)list.get(i)).getKey().toString(),((Map.Entry)englishList.get(i)).getKey().toString());
        }
        return key;
    }

    private  String getCypherText(HashMap<String,String> key){
        boolean[] isChanged = new boolean[input_text.length()];
        StringBuilder outputText = new StringBuilder(input_text);

        for(Map.Entry lines:key.entrySet()){
//            System.out.print(lines.getKey().toString() + " " + lines.getValue().toString());
//            System.out.println();
            if(lines.getKey().toString().equals(lines.getValue().toString())) continue;
            int index = outputText.indexOf(lines.getKey().toString());
            while(index!=-1){
                if(!isChanged[index]) {
                    outputText = outputText.replace(index,index+1,lines.getValue().toString());
                    isChanged[index] = true;
                    index = outputText.indexOf(lines.getKey().toString());
                }
                else
                index = outputText.indexOf(lines.getKey().toString(),index+1);
            }
        }
//        System.out.println(input_text.indexOf(""));
//        System.out.println(Arrays.toString(isChanged));
//        for(int i=0;i<400;i++)
//            if(!isChanged[i]) System.out.println(outputText.charAt(i));
    //    System.out.println(outputText);
        return outputText.toString();
    }

    private Double countStat(HashMap<String,String> key){
        String cypherText = getCypherText(key);
        Map<String, Double> stat = new HashMap<String, Double>();
        for(int i=0;i<cypherText.length()-3;i++) {
            String subString = cypherText.substring(i, i + 3);
            if (!stat.containsKey(subString)) {
                stat.put(subString, 1.0);
            } else {
                Double variable = stat.get(subString);
                variable++;
                stat.replace(subString, variable);
            }
        }

        for(Map.Entry lines:stat.entrySet()){
            Double var = ((Double) lines.getValue());
            var/=input_text.length();
            lines.setValue(var);
        }
        Double sum = 0.0;
        for(Map.Entry lines:stat.entrySet()){
            if(trigramStatistic.get(lines.getKey().toString())==null)
                sum += Math.pow(((Double) lines.getValue()), 2) / ((Double) lines.getValue());
            else {
                sum += Math.pow(((Double) lines.getValue()) - trigramStatistic.get(lines.getKey().toString()), 2) / ((Double) lines.getValue());
            }
        }
        return sum;
    }
}
