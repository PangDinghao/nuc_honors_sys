package com.nuc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.sys.mapper.CollegeMapper;
import com.nuc.sys.mapper.DepartmentMapper;
import com.nuc.sys.mapper.UserMapper;
import com.nuc.sys.pojo.College;
import com.nuc.sys.pojo.Department;
import com.nuc.sys.pojo.User;
import com.nuc.sys.pojo.UserDTO;
import com.nuc.sys.service.CollegeService;
import com.nuc.sys.service.DepartmentService;
import com.nuc.sys.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private CollegeService collegeService;
    @Autowired
    private DepartmentService departmentService;

    @Override
    public Page<UserDTO> getUserDtoPage(Page<User> pageUser) {

        //将userpage对象的属性复制到userdto属性上
        Page<UserDTO> userDTOPage = new Page<>();
        BeanUtils.copyProperties(pageUser,userDTOPage);

        //从page对象中获取到user的list
        List<User> records = pageUser.getRecords();
        List<UserDTO> userDTOS = new ArrayList<>();


        //循环user,查询到每个用户的学院和专业信息封装到userdto中
        for (User user : records) {
            UserDTO userDTO = new UserDTO();
            //将user复制到userdto上
            BeanUtils.copyProperties(user,userDTO);
            College college = collegeService.getById(userDTO.getCollegeId());
            Department department = departmentService.getById(userDTO.getDepartmentId());

            userDTO.setCollegeName(college.getName());
            userDTO.setDepartmentName(department.getName());
            //查询后的userdto放到list当中
            userDTOS.add(userDTO);
        }
        //将userDtolist放入page对象
        userDTOPage.setRecords(userDTOS);

        return userDTOPage;
    }

    @Override
    public UserDTO getDtoById(int id) {
        User user =  this.getById(id);
        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(user,userDTO);

        College college = collegeService.getById(userDTO.getCollegeId());
        Department department = departmentService.getById(userDTO.getDepartmentId());

        userDTO.setCollegeName(college.getName());
        userDTO.setDepartmentName(department.getName());

        return userDTO;
    }

    @Override
    public List<UserDTO> getUserDtoList(List<User> list) {

        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user : list) {
            UserDTO userDTO = new UserDTO();
            //将user复制到userdto上
            BeanUtils.copyProperties(user,userDTO);
            College college = collegeService.getById(userDTO.getCollegeId());
            Department department = departmentService.getById(userDTO.getDepartmentId());

            String sex = user.getSex();
            if (sex.equals("1")){
                userDTO.setSex("男");
            }else if(sex.equals("0")){
                userDTO.setSex("女");
            }

            long roleId = user.getRoleId();
            if (roleId==1){
                userDTO.setRoleName("教师");
            }else if (roleId==2){
                userDTO.setRoleName("学生");
            }else if(roleId==0){
                userDTO.setRoleName("网站管理员");
            }

            userDTO.setCollegeName(college.getName());
            userDTO.setDepartmentName(department.getName());
            //查询后的userdto放到list当中
            userDTOS.add(userDTO);
        }

        return userDTOS;
    }

    @Override
    public boolean SaveImportUser(List<UserDTO> users) {

        for (UserDTO user : users) {
            String roleName = user.getRoleName();
            String collegeName = user.getCollegeName();
            String departmentName = user.getDepartmentName();
            
            if (roleName.equals("超级管理员")){
                user.setRoleId(0);
            } else if (roleName.equals("教师")) {
                user.setRoleId(1);
            } else if (roleName.equals("学生")) {
                user.setRoleId(2);
            }

            String sex = user.getSex();
            if (sex.equals("男")){
                user.setSex("1");
            }else if(sex.equals("女")){
                user.setSex("0");
            }

            LambdaQueryWrapper<College> collegeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            collegeLambdaQueryWrapper.eq(College::getName, collegeName);
            College college = collegeService.getOne(collegeLambdaQueryWrapper);
            user.setCollegeId(college.getId());

            LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            departmentLambdaQueryWrapper.eq(Department::getName, departmentName);
            Department department = departmentService.getOne(departmentLambdaQueryWrapper);
            user.setCollegeId(department.getId());

            this.save(user);
        }
        return true;
    }
}
