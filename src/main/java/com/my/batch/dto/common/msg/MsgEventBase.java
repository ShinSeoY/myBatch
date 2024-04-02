package com.my.batch.dto.common.msg;

import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MsgEventBase {

    Notification notification;
    Message message;

}
