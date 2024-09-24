package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/**
 *  荣誉奖项类型实体类
 */
@Data
public class HonorType {
  @TableId(type = IdType.AUTO)
  private long id;
  private String name;
  private String detail;

}
