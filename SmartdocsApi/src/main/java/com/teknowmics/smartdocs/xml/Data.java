/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teknowmics.smartdocs.xml;

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
    "currentPage",
    "totalNumItems",
    "totalNumPages",
    "pageSize",
    "items"
})
public class Data {

    private Integer currentPage;
    private Integer totalNumItems;
    private Integer totalNumPages;
    private Integer pageSize;

    @XmlElement(required = true)
    private Items items;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer value) {
        this.currentPage = value;
    }

    public Integer getTotalNumItems() {
        return totalNumItems;
    }

    public void setTotalNumItems(Integer value) {
        this.totalNumItems = value;
    }

    public Integer getTotalNumPages() {
        return totalNumPages;
    }

    public void setTotalNumPages(Integer value) {
        this.totalNumPages = value;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer value) {
        this.pageSize = value;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items value) {
        this.items = value;
    }

}
