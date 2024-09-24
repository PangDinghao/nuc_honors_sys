package com.nuc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nuc.sys.common.R;
import com.nuc.sys.pojo.Message;
import com.nuc.sys.pojo.MessageDTO;
import com.nuc.sys.pojo.User;
import com.nuc.sys.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    HttpSession session;

    // 发送消息/通知
    @PostMapping("/send")
    public R send(Message message) {
        User user = (User) session.getAttribute("currentUser");
        message.setSendUserid(user.getId());
        message.setSendDatatime(Timestamp.valueOf(LocalDateTime.now()));

        boolean flag = messageService.save(message);

        if (flag) {
            return R.ok("发送成功");
        } else {
            return R.error("发送失败");
        }
    }

    // 获取消息/通知列表
    @GetMapping("/list")
    public R list(Message message) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getReceiveUserid, message.getReceiveUserid());
        queryWrapper.orderByDesc(Message::getSendDatatime);


        List<Message> messageList = messageService.list(queryWrapper);
        List<MessageDTO> messageDTOList = messageService.getMessageDTOList(messageList);


        return R.ok("获取成功").put("messageList", messageDTOList);
    }

    // 标记已读/查看消息通知

    @GetMapping("/read")
    public R read(Integer id) {
        Message message = messageService.getById(id);
        message.setStatus(1);
        messageService.updateById(message);

        MessageDTO messageDTO = messageService.getMessageDTO(message);


        return R.ok("查看成功").put("messageDTO", messageDTO);
    }



}
