

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlbumList", propOrder = {
        "album"
})
public class AlbumList {

    protected List<Child> album;

    public List<Child> getAlbum() {
        if (album == null) {
            album = new ArrayList<Child>();
        }
        return this.album;
    }

}
