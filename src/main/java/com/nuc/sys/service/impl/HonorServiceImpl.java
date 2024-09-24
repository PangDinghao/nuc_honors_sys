package com.nuc.sys.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.sys.mapper.*;
import com.nuc.sys.pojo.Honor;
import com.nuc.sys.pojo.HonorDTO;
import com.nuc.sys.service.HonorService;
import com.nuc.sys.service.HonorTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HonorServiceImpl extends ServiceImpl<HonorMapper, Honor> implements HonorService {
    @Autowired
    HonorMapper honorMapper;
    @Autowired
    HonorTypeMapper honorTypeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CollegeMapper collegeMapper;
    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public Page<HonorDTO> getHonorDtoPage(Page<Honor> honorPage) {
        Page<HonorDTO> honorDTOPage = new Page<>();
        BeanUtils.copyProperties(honorPage, honorDTOPage);

        List<Honor> honors = honorPage.getRecords();
        List<HonorDTO> honorDTOS = new ArrayList<>();


        for (Honor honor : honors) {
            HonorDTO honorDTO = new HonorDTO();
            BeanUtils.copyProperties(honor, honorDTO);
            //获取类型名字
            honorDTO.setTypeName(honorTypeMapper.selectById(honor.getTypeId()).getName());
            //获取创建和修改者用户名
            honorDTO.setCreateUsername(userMapper.selectById(honor.getCreateUserid()).getName());
            if (honor.getUpdateUserid()!= 0) {
                honorDTO.setUpdateUsername(userMapper.selectById(honor.getUpdateUserid()).getName());
            }

            //获取学院名字
            honorDTO.setCollegeName(collegeMapper.selectById(honor.getCollegeId()).getName());
            //获取专业名字
            honorDTO.setDeparmentName(departmentMapper.selectById(honor.getDepartmentId()).getName());

            //添加到honorDTOS
            honorDTOS.add(honorDTO);
        }
        honorDTOPage.setRecords(honorDTOS);

        return honorDTOPage;
    }

    @Override
    public HonorDTO getHonorDtoById(Integer id) {
        Honor honor = honorMapper.selectById(id);
        HonorDTO honorDTO = new HonorDTO();

        BeanUtils.copyProperties(honor, honorDTO);
        //获取类型名字
        honorDTO.setTypeName(honorTypeMapper.selectById(honor.getTypeId()).getName());
        //获取创建和修改者用户名
        honorDTO.setCreateUsername(userMapper.selectById(honor.getCreateUserid()).getName());
        if (honor.getUpdateUserid()!= 0) {
            honorDTO.setUpdateUsername(userMapper.selectById(honor.getUpdateUserid()).getName());
        }
        //获取学院名字
        honorDTO.setCollegeName(collegeMapper.selectById(honor.getCollegeId()).getName());
        //获取专业名字
        honorDTO.setDeparmentName(departmentMapper.selectById(honor.getDepartmentId()).getName());

        return honorDTO;
    }
}
