package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;

/**
 *  新闻公告实体类
 */
@Data
public class New {
  @TableId(type = IdType.AUTO)
  private long id;
  private String title;
  private String coverImg;
  private long type;
  private String detail;
  private LocalDateTime createDatatime;
  private long createUserid;
  private LocalDateTime updateDatatime;
  private long updateUserid;
  private int hit;
}
