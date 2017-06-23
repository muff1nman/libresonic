

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArtistInfo", propOrder = {
        "similarArtist"
})
public class ArtistInfo
        extends ArtistInfoBase {

    protected List<Artist> similarArtist;

    public List<Artist> getSimilarArtist() {
        if (similarArtist == null) {
            similarArtist = new ArrayList<Artist>();
        }
        return this.similarArtist;
    }

}
