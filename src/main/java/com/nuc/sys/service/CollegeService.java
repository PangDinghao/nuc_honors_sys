package com.nuc.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.sys.pojo.College;
import com.nuc.sys.pojo.CollegeDTO;

public interface CollegeService extends IService<College> {
    boolean deleteById(Integer id);

    Page<CollegeDTO> getCollegeDtoPage(Page<College> collegePage);

    boolean saveCollegeDTO(CollegeDTO collegeDTO);

    boolean updatecollegeDTO(CollegeDTO collegeDTO);
}
