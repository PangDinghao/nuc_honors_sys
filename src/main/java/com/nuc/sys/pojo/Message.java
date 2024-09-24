package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/**
 *  消息实体类
 */
@Data
public class Message {
  @TableId(type = IdType.AUTO)
  private long id;
  private String title;
  private String details;
  private java.sql.Timestamp sendDatatime;
  private long sendUserid;
  private java.sql.Timestamp receiveDatatime;
  private long receiveUserid;
  private long status;


}
