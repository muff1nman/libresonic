//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.16 at 09:52:59 AM MDT 
//


package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArtistsID3", propOrder = {
    "index"
})
public class ArtistsID3 {

    protected List<IndexID3> index;
    @XmlAttribute(name = "ignoredArticles", required = true)
    protected String ignoredArticles;

    /**
     * Gets the value of the index property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the index property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndex().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndexID3 }
     *
     *
     */
    public List<IndexID3> getIndex() {
        if (index == null) {
            index = new ArrayList<IndexID3>();
        }
        return this.index;
    }

    /**
     * Gets the value of the ignoredArticles property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIgnoredArticles() {
        return ignoredArticles;
    }

    /**
     * Sets the value of the ignoredArticles property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIgnoredArticles(String value) {
        this.ignoredArticles = value;
    }

}
