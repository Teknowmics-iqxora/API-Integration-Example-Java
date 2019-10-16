package com.teknowmics.smartdocs.xml;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "body"
})
@XmlRootElement(name = "result")
public class Result {

    @XmlElement(required = true)
    private Header header;

    private Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        this.header = value;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body value) {
        this.body = value;
    }
}
