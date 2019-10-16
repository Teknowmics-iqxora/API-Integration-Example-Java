/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teknowmics.smartdocs.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author administrator
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "item"
})
public class Items {

    @XmlElement(required = true)
    private List<Item> item;

    public List<Item> getItem() {
        if (item == null) {
            item = new ArrayList<>();
        }
        return this.item;
    }
    
    public void setItem(List<Item> item) {
        this.item = item;
    }

}
