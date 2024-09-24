package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *  专业实体类
 */
@Data
public class DepartmentDTO extends Department{
  private String collegeName;

}
