package com.nuc.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.sys.mapper.MessageMapper;
import com.nuc.sys.mapper.NewMapper;
import com.nuc.sys.mapper.UserMapper;
import com.nuc.sys.pojo.Message;
import com.nuc.sys.pojo.MessageDTO;
import com.nuc.sys.pojo.New;
import com.nuc.sys.pojo.User;
import com.nuc.sys.service.MessageService;
import com.nuc.sys.service.NewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public MessageDTO getMessageDTO(Message message) {
        User user = userMapper.selectById(message.getSendUserid());
        MessageDTO messageDTO = new MessageDTO();

        BeanUtils.copyProperties(message, messageDTO);
        messageDTO.setSenderName(user.getName());

        return messageDTO;
    }

    @Override
    public List<MessageDTO> getMessageDTOList(List<Message> messageList) {
        List<MessageDTO> messageDTOList = new ArrayList<>();

        for (Message message: messageList) {

            long sendUserid = message.getSendUserid();
            User user = userMapper.selectById(sendUserid);
            MessageDTO messageDTO = new MessageDTO();
            BeanUtils.copyProperties(message, messageDTO);
            messageDTO.setSenderName(user.getName());
            messageDTOList.add(messageDTO);
        }

        return messageDTOList;
    }
}
