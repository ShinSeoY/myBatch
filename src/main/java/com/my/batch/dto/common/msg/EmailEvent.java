package com.my.batch.dto.common.msg;

import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailEvent extends MsgEventBase {

    public EmailEvent(Notification notification, Message message) {
        super(notification, message);
    }

}
