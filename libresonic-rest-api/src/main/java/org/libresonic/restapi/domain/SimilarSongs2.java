

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimilarSongs2", propOrder = {
        "song"
})
public class SimilarSongs2 {

    protected List<Child> song;

    public List<Child> getSong() {
        if (song == null) {
            song = new ArrayList<Child>();
        }
        return this.song;
    }

}
