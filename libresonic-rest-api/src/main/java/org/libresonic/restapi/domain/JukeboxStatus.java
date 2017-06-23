//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.16 at 09:52:59 AM MDT 
//


package org.libresonic.restapi.domain;



import javax.xml.bind.annotation.*;


/**
 * <p>Java class for JukeboxStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JukeboxStatus"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="currentIndex" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="playing" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="gain" use="required" type="{http://www.w3.org/2001/XMLSchema}float" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JukeboxStatus")
@XmlSeeAlso({
    JukeboxPlaylist.class
})
public class JukeboxStatus {

    @XmlAttribute(name = "currentIndex", required = true)
    protected int currentIndex;
    @XmlAttribute(name = "playing", required = true)
    protected boolean playing;
    @XmlAttribute(name = "gain", required = true)
    protected float gain;
    @XmlAttribute(name = "position")
    protected Integer position;

    /**
     * Gets the value of the currentIndex property.
     * 
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Sets the value of the currentIndex property.
     * 
     */
    public void setCurrentIndex(int value) {
        this.currentIndex = value;
    }

    /**
     * Gets the value of the playing property.
     * 
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets the value of the playing property.
     * 
     */
    public void setPlaying(boolean value) {
        this.playing = value;
    }

    /**
     * Gets the value of the gain property.
     * 
     */
    public float getGain() {
        return gain;
    }

    /**
     * Sets the value of the gain property.
     * 
     */
    public void setGain(float value) {
        this.gain = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPosition(Integer value) {
        this.position = value;
    }

}
