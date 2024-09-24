package com.nuc.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.nuc.sys.utils.ExcelImport;
import lombok.Data;

/**
 *  用户综合类
 */
@Data
public class UserDTO extends User{
    @TableId(type = IdType.AUTO)
    private long id;
    @ExcelImport("姓名")
    private String name;
    @ExcelImport("年龄")
    private long age;
    @ExcelImport("性别")
    private String sex;
    @ExcelImport("联系电话")
    private long phone;
    private long stuNumber;
    @ExcelImport("邮箱")
    private String email;
    @ExcelImport("密码")
    private String password;
    private long collegeId;
    private long departmentId;
    private long roleId;
    //学院名字
    @ExcelImport("学院")
    private String collegeName;

    //部门名字
    @ExcelImport("专业")
    private String departmentName;

    //角色
    @ExcelImport("角色")
    private String roleName;

}
