

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Error")
public class Error {

    @XmlAttribute(name = "code", required = true)
    protected int code;
    @XmlAttribute(name = "message")
    protected String message;

    public int getCode() {
        return code;
    }

    public void setCode(int value) {
        this.code = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

}
