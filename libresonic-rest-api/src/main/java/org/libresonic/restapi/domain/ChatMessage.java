
package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChatMessage")
public class ChatMessage {

    @XmlAttribute(name = "username", required = true)
    protected String username;
    @XmlAttribute(name = "time", required = true)
    protected long time;
    @XmlAttribute(name = "message", required = true)
    protected String message;

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long value) {
        this.time = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

}
