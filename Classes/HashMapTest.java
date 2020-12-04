package Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ListIterator;

public class HashMapTest {
    public static void main(String[] args) {
//        int turns = 4;
//        while(turns > 0){
//            System.out.println(turns);
//            turns--;
//        }

        LinkedHashMap<String, int[]> hashMap = new LinkedHashMap<>();
        putToHashMap(hashMap, "ala", 2);
        putToHashMap(hashMap, "ala", 3);
        putToHashMap(hashMap, "kamil", 4);
        putToHashMap(hashMap, "kuba", 5);
        putToHashMap(hashMap, "ala", 6);
        showHashMapWhile(hashMap);
        removeFromHashMap(hashMap, "ala", 3);
        removeFromHashMap(hashMap, "kamil", 4);
        showHashMapWhile(hashMap);

        // while z iteratorem vs fory | => mozna zamienic na fory, tak będzie czytelniej



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

    public static void showHashMapFor(LinkedHashMap hashMap){
        ArrayList<ArrayList> arrayOfArrays = returnArray(hashMap);

        for(ArrayList list: arrayOfArrays){
            if(!list.isEmpty()){
                for(Object element: list){
                    System.out.print(element);
                }
                System.out.println(" ");
            }
        }
    }

    public static void showHashMapWhile(LinkedHashMap hashMap){
        ArrayList<ArrayList> arrayOfArrays = new ArrayList<>( hashMap.values() );

        ListIterator<ArrayList> animalsArrayListIterator = arrayOfArrays.listIterator();

        while (animalsArrayListIterator.hasNext()) {
            ArrayList<Integer> animalsAtPosition = animalsArrayListIterator.next();
            ListIterator<Integer> animalListIterator = animalsAtPosition.listIterator();
            while (animalListIterator.hasNext()) {
                Integer i = animalListIterator.next();
                System.out.print(i);
            }
            System.out.println(" ");
        }
    }

    public static ArrayList<ArrayList> returnArray(LinkedHashMap hashMap){
        return new ArrayList<>( hashMap.values() );
    }
}
