package com.nuc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.sys.mapper.CollegeMapper;
import com.nuc.sys.mapper.DepartmentMapper;
import com.nuc.sys.pojo.College;
import com.nuc.sys.pojo.Department;
import com.nuc.sys.pojo.DepartmentDTO;
import com.nuc.sys.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private CollegeMapper collegeMapper;
    @Override
    public Page<DepartmentDTO> getDepartmentDTOPage(Page<Department> departmentPage) {
        List<Department> departments = departmentPage.getRecords();
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();

        Page<DepartmentDTO> departmentDTOPage = new Page<>();
        BeanUtils.copyProperties(departmentPage, departmentDTOPage);


        for (Department department : departments) {

            DepartmentDTO departmentDTO = new DepartmentDTO();
            // 这里需要将department的属性复制到departmentDTO中
            BeanUtils.copyProperties(department, departmentDTO);
            // 这里需要将college查询到并将name属性复制到departmentDTO中
            LambdaQueryWrapper<College> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(College::getId, department.getCollegeId());
            College college = collegeMapper.selectOne(queryWrapper);
            if (college != null) {
                departmentDTO.setCollegeName(college.getName());
            }

            //将departmentDTO添加到departmentDTOS中
            departmentDTOS.add(departmentDTO);
        }

        // 使用 Comparator 对 departmentDTOS 进行排序
        Collections.sort(departmentDTOS, new Comparator<DepartmentDTO>() {
            @Override
            public int compare(DepartmentDTO dto1, DepartmentDTO dto2) {
                // 根据 collegeId 进行排序
                return Long.compare(dto1.getCollegeId(), dto2.getCollegeId());
            }
        });

        departmentDTOPage.setRecords(departmentDTOS);

        return departmentDTOPage;
    }
}
