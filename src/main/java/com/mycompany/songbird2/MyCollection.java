/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author HP
 */
class MyCollection implements Iterable<String>{
    private ArrayList<String> values = new ArrayList<>();
    public MyCollection(ArrayList<String> v) {
        this.values=v;
        }
    public String get(int index){
        return values.get(index);
    }
    public ArrayList<String> getArray(){
        return values;
    }
    public int size(){
        return values.size();
    }
    @Override
    public Iterator<String> iterator() {
        return new MyIterator(this);
    }

    
    
}
