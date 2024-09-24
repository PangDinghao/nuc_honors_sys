package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *  消息实体类
 */
@Data
public class MessageDTO extends Message {

    private String senderName; // 发送者姓名


}
