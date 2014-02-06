/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public abstract class Statement {
    
    public abstract Path estimatePath();
    
    @Override
    public abstract String toString();
    
}
