import java.util.HashMap;

/**
 * Created by Maria on 05.10.2016.
 */
public class Key {

    HashMap<String,String> oneToOne = new HashMap<>();
    String TextKey;
    Key(HashMap<String,String> oneToOne,String TextKey){
        this.oneToOne = (HashMap<String,String>)oneToOne.clone();
        this.TextKey = TextKey;

    }
}
