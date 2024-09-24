package com.nuc.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.sys.pojo.Department;
import com.nuc.sys.pojo.DepartmentDTO;
import com.nuc.sys.pojo.HonorType;

public interface DepartmentService extends IService<Department> {
    Page<DepartmentDTO> getDepartmentDTOPage(Page<Department> departmentPage);
}
