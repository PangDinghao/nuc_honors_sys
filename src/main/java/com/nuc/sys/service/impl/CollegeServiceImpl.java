package com.nuc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.sys.mapper.CollegeMapper;
import com.nuc.sys.mapper.DepartmentMapper;
import com.nuc.sys.pojo.College;
import com.nuc.sys.pojo.CollegeDTO;
import com.nuc.sys.pojo.Department;
import com.nuc.sys.service.CollegeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {
    @Autowired
    CollegeMapper collegeMapper;
    @Autowired
    DepartmentMapper departmentMapper;

    //删除学院（先判断学院下是否有专业系别，如果有则不能删除）
    @Override
    public boolean deleteById(Integer id) {
        College college = collegeMapper.selectById(id);

        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getCollegeId, college.getId());
        List<Department> departments = departmentMapper.selectList(lambdaQueryWrapper);

        if (departments.isEmpty()){
            collegeMapper.deleteById(id);
            return true;
        }else {
            return false;
        }

    }

    //将学院下的专业系别查询并存到dto当中
    @Override
    public Page<CollegeDTO> getCollegeDtoPage(Page<College> collegePage) {
        Page<CollegeDTO> collegeDTOPage = new Page<>();
        BeanUtils.copyProperties(collegePage,collegeDTOPage);

        List<College> colleges = collegePage.getRecords();
        List<CollegeDTO> collegeDTOS = new ArrayList<>();


        for (College college : colleges) {
            LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Department::getCollegeId,college.getId());
            List<Department> departments = departmentMapper.selectList(lambdaQueryWrapper);

            CollegeDTO collegeDTO = new CollegeDTO();
            BeanUtils.copyProperties(college, collegeDTO);
            collegeDTO.setDepartments(departments);

            collegeDTOS.add(collegeDTO);
        }

        collegeDTOPage.setRecords(collegeDTOS);

        return collegeDTOPage;
    }

    @Override
    public boolean saveCollegeDTO(CollegeDTO collegeDTO) {
        College college = new College();
        BeanUtils.copyProperties(collegeDTO, college);
        int collegeId = collegeMapper.insert(college);

        List<Department> departments = collegeDTO.getDepartments();
        for (Department department : departments) {
            department.setCollegeId(college.getId());
            departmentMapper.updateById(department);
        }

        return true;
    }

    @Override
    public boolean updatecollegeDTO(CollegeDTO collegeDTO) {
        College college = new College();
        BeanUtils.copyProperties(collegeDTO, college);
        collegeMapper.updateById(college);
        //先将原有专业的collegeId置为0，再将新传入的专业的collegeId置为传入的学院的id
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getCollegeId, collegeDTO.getId());
        List<Department> departments = departmentMapper.selectList(lambdaQueryWrapper);

        for (Department department : departments) {
            department.setCollegeId(0);
            departmentMapper.updateById(department);
        }

        List<Department> Newdepartments = collegeDTO.getDepartments();
        for (Department department : Newdepartments) {
            department.setCollegeId(college.getId());
            departmentMapper.updateById(department);
        }

        return true;
    }

}
