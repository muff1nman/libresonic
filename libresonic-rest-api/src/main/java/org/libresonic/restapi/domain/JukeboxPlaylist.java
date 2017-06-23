

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JukeboxPlaylist", propOrder = {
        "entry"
})
public class JukeboxPlaylist
        extends JukeboxStatus {

    protected List<Child> entry;

    public List<Child> getEntry() {
        if (entry == null) {
            entry = new ArrayList<Child>();
        }
        return this.entry;
    }

}
