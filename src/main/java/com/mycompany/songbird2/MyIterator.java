/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

import java.util.Iterator;

/**
 *
 * @author HP
 */
public class MyIterator implements Iterator<String>{
    
    private MyCollection col;
    private int index;
    public MyIterator(MyCollection m){
        this.col = m;
        this.index = 0;
    }
    @Override
    public boolean hasNext() {
        return index < col.size();
    }

    @Override
    public String next() {
        return col.get(index++);
    }
    
}
