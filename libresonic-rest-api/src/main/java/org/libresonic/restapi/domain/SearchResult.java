

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchResult", propOrder = {
        "match"
})
public class SearchResult {

    protected List<Child> match;
    @XmlAttribute(name = "offset", required = true)
    protected int offset;
    @XmlAttribute(name = "totalHits", required = true)
    protected int totalHits;

    public List<Child> getMatch() {
        if (match == null) {
            match = new ArrayList<Child>();
        }
        return this.match;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int value) {
        this.offset = value;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int value) {
        this.totalHits = value;
    }

}
