package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *  荣誉奖项实体类
 */
@Data
public class Honor {
  @TableId(type = IdType.AUTO)
  private long id;
  private String name;
  private long typeId;
  private String detail;
  private String picture;
  private String stuCard;
  private java.sql.Date awardData;
  private java.sql.Timestamp createDatatime;
  private long createUserid;
  private java.sql.Timestamp updateDatatime;
  private long updateUserid;
  private long status;
  private long collegeId;
  private long departmentId;

}
