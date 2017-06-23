

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlayQueue", propOrder = {
        "entry"
})
public class PlayQueue {

    protected List<Child> entry;
    @XmlAttribute(name = "current")
    protected Integer current;
    @XmlAttribute(name = "position")
    protected Long position;
    @XmlAttribute(name = "username", required = true)
    protected String username;
    @XmlAttribute(name = "changed", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar changed;
    @XmlAttribute(name = "changedBy", required = true)
    protected String changedBy;

    public List<Child> getEntry() {
        if (entry == null) {
            entry = new ArrayList<Child>();
        }
        return this.entry;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer value) {
        this.current = value;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long value) {
        this.position = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public XMLGregorianCalendar getChanged() {
        return changed;
    }

    public void setChanged(XMLGregorianCalendar value) {
        this.changed = value;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String value) {
        this.changedBy = value;
    }

}
