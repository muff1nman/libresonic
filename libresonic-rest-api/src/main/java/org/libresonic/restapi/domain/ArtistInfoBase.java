

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArtistInfoBase", propOrder = {
        "biography",
        "musicBrainzId",
        "lastFmUrl",
        "smallImageUrl",
        "mediumImageUrl",
        "largeImageUrl"
})
@XmlSeeAlso({
        ArtistInfo.class,
        ArtistInfo2.class
})
public class ArtistInfoBase {

    protected String biography;
    protected String musicBrainzId;
    protected String lastFmUrl;
    protected String smallImageUrl;
    protected String mediumImageUrl;
    protected String largeImageUrl;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String value) {
        this.biography = value;
    }

    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public void setMusicBrainzId(String value) {
        this.musicBrainzId = value;
    }

    public String getLastFmUrl() {
        return lastFmUrl;
    }

    public void setLastFmUrl(String value) {
        this.lastFmUrl = value;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String value) {
        this.smallImageUrl = value;
    }

    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    public void setMediumImageUrl(String value) {
        this.mediumImageUrl = value;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String value) {
        this.largeImageUrl = value;
    }

}
