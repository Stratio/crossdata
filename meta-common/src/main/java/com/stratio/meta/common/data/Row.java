package com.stratio.meta.common.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Row implements Serializable {

    private Map<String, Cell> cells;

    public Row() {
        cells = new HashMap<>();
    }

    public int size(){
        return cells.size();
    }

    public Map<String, Cell> getCells() {
        return cells;
    }

    public void setCells(Map<String, Cell> cells) {
        this.cells = cells;
    }

    public void addCell(String key, Cell cell){
        cells.put(key, cell);
    }

    public Cell getCell(String key){
        return cells.get(key);
    }

}
