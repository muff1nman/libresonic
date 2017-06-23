

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VideoConversion")
public class VideoConversion {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "bitRate")
    protected Integer bitRate;
    @XmlAttribute(name = "audioTrackId")
    protected Integer audioTrackId;

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer value) {
        this.bitRate = value;
    }

    public Integer getAudioTrackId() {
        return audioTrackId;
    }

    public void setAudioTrackId(Integer value) {
        this.audioTrackId = value;
    }

}
