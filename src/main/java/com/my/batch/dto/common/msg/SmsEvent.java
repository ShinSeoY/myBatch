package com.my.batch.dto.common.msg;

import com.my.batch.common.utils.CryptoDbUtil;
import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsEvent extends MsgEventBase {

    public SmsEvent(CryptoDbUtil cryptoDbUtil, Notification notification, Message message) {
        super(notification, message);
        try {
            notification.getMember().setPlainPhone(cryptoDbUtil.decrypt(notification.getMember().getPhone()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
