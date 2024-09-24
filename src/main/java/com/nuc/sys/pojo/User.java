package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.nuc.sys.utils.ExcelImport;
import lombok.Data;

/**
 *  用户实体类
 */
@Data
public class User {
  @TableId(type = IdType.AUTO)
  private long id;
  private String name;
  private long age;
  private String sex;
  private long phone;
  private long stuNumber;
  private String email;
  private String password;
  private long collegeId;
  private long departmentId;
  private long roleId;
}
