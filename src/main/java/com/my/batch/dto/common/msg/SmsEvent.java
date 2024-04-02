package com.my.batch.dto.common.msg;

import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsEvent extends MsgEventBase {

    public SmsEvent(Notification notification, Message message) {
        super(notification, message);
    }
}
