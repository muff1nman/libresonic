

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Share", propOrder = {
        "entry"
})
public class Share {

    protected List<Child> entry;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "url", required = true)
    protected String url;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "username", required = true)
    protected String username;
    @XmlAttribute(name = "created", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;
    @XmlAttribute(name = "expires")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expires;
    @XmlAttribute(name = "lastVisited")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastVisited;
    @XmlAttribute(name = "visitCount", required = true)
    protected int visitCount;

    public List<Child> getEntry() {
        if (entry == null) {
            entry = new ArrayList<Child>();
        }
        return this.entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    public XMLGregorianCalendar getExpires() {
        return expires;
    }

    public void setExpires(XMLGregorianCalendar value) {
        this.expires = value;
    }

    public XMLGregorianCalendar getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(XMLGregorianCalendar value) {
        this.lastVisited = value;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int value) {
        this.visitCount = value;
    }

}
