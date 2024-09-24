package com.nuc.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.sys.pojo.Message;
import com.nuc.sys.pojo.MessageDTO;
import com.nuc.sys.pojo.New;

import java.util.List;

public interface MessageService extends IService<Message> {
    MessageDTO getMessageDTO(Message message);

    List<MessageDTO> getMessageDTOList(List<Message> messageList);
}
