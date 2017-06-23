

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bookmark", propOrder = {
        "entry"
})
public class Bookmark {

    @XmlElement(required = true)
    protected Child entry;
    @XmlAttribute(name = "position", required = true)
    protected long position;
    @XmlAttribute(name = "username", required = true)
    protected String username;
    @XmlAttribute(name = "comment")
    protected String comment;
    @XmlAttribute(name = "created", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;
    @XmlAttribute(name = "changed", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar changed;

    public Child getEntry() {
        return entry;
    }

    public void setEntry(Child value) {
        this.entry = value;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long value) {
        this.position = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    public XMLGregorianCalendar getChanged() {
        return changed;
    }

    public void setChanged(XMLGregorianCalendar value) {
        this.changed = value;
    }

}
