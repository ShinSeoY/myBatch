package com.my.batch.dto.common.msg;

import com.my.batch.common.utils.CryptoDbUtil;
import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsEvent extends MsgEventBase {

    public SmsEvent(CryptoDbUtil cryptoDbUtil, Notification notification, Message message, String encPhone) {
        super(notification, message);
        try {
            if (notification != null && encPhone == null) {
                notification.getMember().setPlainPhone(cryptoDbUtil.decrypt(notification.getMember().getPhone()));
            } else {
                message.setPlainPhone(cryptoDbUtil.decrypt(encPhone));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
