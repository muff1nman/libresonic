

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VideoInfo", propOrder = {
        "captions",
        "audioTrack",
        "conversion"
})
public class VideoInfo {

    protected List<Captions> captions;
    protected List<AudioTrack> audioTrack;
    protected List<VideoConversion> conversion;
    @XmlAttribute(name = "id", required = true)
    protected String id;

    public List<Captions> getCaptions() {
        if (captions == null) {
            captions = new ArrayList<Captions>();
        }
        return this.captions;
    }

    public List<AudioTrack> getAudioTrack() {
        if (audioTrack == null) {
            audioTrack = new ArrayList<AudioTrack>();
        }
        return this.audioTrack;
    }

    public List<VideoConversion> getConversion() {
        if (conversion == null) {
            conversion = new ArrayList<VideoConversion>();
        }
        return this.conversion;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
