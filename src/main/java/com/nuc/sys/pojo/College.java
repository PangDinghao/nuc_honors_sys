package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/**
 *  学院实体类
 */
@Data
public class College {

  @TableId(type = IdType.AUTO)
  private long id;
  private String name;
  private String detail;

}
