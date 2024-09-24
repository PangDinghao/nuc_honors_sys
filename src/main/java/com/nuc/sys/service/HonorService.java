package com.nuc.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.sys.pojo.Honor;
import com.nuc.sys.pojo.HonorDTO;
import com.nuc.sys.pojo.HonorType;

public interface HonorService extends IService<Honor> {
    Page<HonorDTO> getHonorDtoPage(Page<Honor> honorPage);

    HonorDTO getHonorDtoById(Integer id);
}
