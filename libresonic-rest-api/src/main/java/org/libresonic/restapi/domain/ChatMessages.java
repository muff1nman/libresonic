

package org.libresonic.restapi.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChatMessages", propOrder = {
        "chatMessage"
})
public class ChatMessages {

    protected List<ChatMessage> chatMessage;

    public List<ChatMessage> getChatMessage() {
        if (chatMessage == null) {
            chatMessage = new ArrayList<ChatMessage>();
        }
        return this.chatMessage;
    }

}
