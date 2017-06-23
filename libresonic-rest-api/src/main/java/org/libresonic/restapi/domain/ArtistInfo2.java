

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArtistInfo2", propOrder = {
        "similarArtist"
})
public class ArtistInfo2
        extends ArtistInfoBase {

    protected List<ArtistID3> similarArtist;

    public List<ArtistID3> getSimilarArtist() {
        if (similarArtist == null) {
            similarArtist = new ArrayList<ArtistID3>();
        }
        return this.similarArtist;
    }

}
