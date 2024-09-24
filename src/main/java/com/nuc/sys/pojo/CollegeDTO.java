package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 *  学院综合类
 */
@Data
public class CollegeDTO {

  @TableId(type = IdType.AUTO)
  private long id;
  private String name;
  private String detail;

  //学院名下的专业系别
  private List<Department> departments;

}
