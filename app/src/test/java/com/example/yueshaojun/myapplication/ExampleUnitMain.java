package com.example.yueshaojun.myapplication;


import android.util.SparseArray;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitMain {
    @Test
    public void addition_isCorrect() throws Exception {
        ArrayList<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("2");
        a.add("3");
        Iterator<String> iterator = a.iterator();
        while(iterator.hasNext()){
            String s = iterator.next();
            if(s.equals("2")){
                iterator.remove();
            }
        }

        //------

//        for(int i = 0;i<a.size();i++){
//            if("2".equals(a.get(i))){
//                a.remove(a.get(i));
//            }
//        }

        //------

//        for(int k = a.size()-1; k>0;k--){
//            if("2".equals(a.get(k))){
//                a.remove(a.get(k));
//            }
//        }
        for(int j = 0 ;j< a.size();j++){
            System.out.println(a.get(j));
        }
    }

    @Test
    public void sparseTest(){
        int hasSwitched = 0;
        int MASK = 1;
        System.out.println(~hasSwitched & MASK);
    }
    @Test
    public void t(){
        long deliveryId = Long.parseLong("275633726823119");
        System.out.println(""+deliveryId);
    }
}