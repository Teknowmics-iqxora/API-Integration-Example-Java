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
    "objectType",
    "keyValue"
})
public class Item {

    @XmlElement(required = true)
    private String objectType;

    private List<KeyValue> keyValue;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String value) {
        this.objectType = value;
    }

    public List<KeyValue> getKeyValue() {
        if (keyValue == null) {
            keyValue = new ArrayList<>();
        }
        return this.keyValue;
    }
    
    public void SetKeyValue(List<KeyValue> keyValue) {
        this.keyValue = keyValue;
    }

}
