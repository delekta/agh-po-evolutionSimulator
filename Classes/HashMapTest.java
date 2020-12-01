package Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class HashMapTest {
    public static void main(String[] args) {
        LinkedHashMap<String, int[]> hashMap = new LinkedHashMap<>();
        putToHashMap(hashMap, "ala", 2);
        putToHashMap(hashMap, "ala", 3);
        putToHashMap(hashMap, "kamil", 4);
        putToHashMap(hashMap, "kuba", 5);
        putToHashMap(hashMap, "ala", 6);
        showHashMap(hashMap);


    }

    public static void putToHashMap(LinkedHashMap hashMap, String key, int value){

        ArrayList<Integer> list = (ArrayList<Integer>) hashMap.get(key);
        if(list == null){
            list = new ArrayList<Integer>();
            hashMap.put(key, list);
        }
        list.add(value);
    }

    public static void removeFromHashMap(LinkedHashMap hashMap, String key, int value){
        ArrayList<Integer> list = (ArrayList<Integer>) hashMap.get(key);
        if(list != null){
            list.remove(value);
        }
        if(list.isEmpty()){
            hashMap.remove(key);
        }
    }

    public static void showHashMap(LinkedHashMap hashMap){
        ArrayList<ArrayList> arrayOfArrays = new ArrayList<>( hashMap.values() );

        for(ArrayList list: arrayOfArrays){
            if(!list.isEmpty()){
                for(Object element: list){
                    System.out.print(element);
                }
                System.out.println(" ");
            }
        }
    }

    public static ArrayList<ArrayList> returnArray(LinkedHashMap hashMap){
        return new ArrayList<>( hashMap.values() );
    }
}
