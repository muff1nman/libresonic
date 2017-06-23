

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PodcastEpisode")
public class PodcastEpisode
        extends Child {

    @XmlAttribute(name = "streamId")
    protected String streamId;
    @XmlAttribute(name = "channelId", required = true)
    protected String channelId;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "status", required = true)
    protected PodcastStatus status;
    @XmlAttribute(name = "publishDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar publishDate;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String value) {
        this.streamId = value;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String value) {
        this.channelId = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public PodcastStatus getStatus() {
        return status;
    }

    public void setStatus(PodcastStatus value) {
        this.status = value;
    }

    public XMLGregorianCalendar getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(XMLGregorianCalendar value) {
        this.publishDate = value;
    }

}
