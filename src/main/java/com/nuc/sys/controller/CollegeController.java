package com.nuc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.sys.common.R;
import com.nuc.sys.pojo.College;
import com.nuc.sys.pojo.CollegeDTO;
import com.nuc.sys.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description 学院
 */
@RestController
@RequestMapping("/college")
public class CollegeController {
    @Autowired
    CollegeService collegeService;

    //添加学院
    @PostMapping("add")
    public R add(@RequestBody CollegeDTO collegeDTO){
        boolean flag = collegeService.saveCollegeDTO(collegeDTO);

        if (flag){
            return R.ok("添加学院成功");
        } else{
            return R.error("添加学院失败");
        }
    }

    //删除学院（先判断学院下是否有专业系别，如果有则不能删除）
    @GetMapping("/delete")
    public R delete(@RequestParam("ids") List<Integer> ids){
        for (int id : ids){
            boolean flag = collegeService.deleteById(id);
            if (!flag) {
                return R.error("删除学院失败,学院下有专业系别");     //如果有学院下存在系别，则不能删除该学院信息
            }
        }

        return R.ok("删除学院成功");

    }

    //修改学院信息
    @PostMapping("/update")
    public R update(@RequestBody CollegeDTO collegeDTO){
        boolean flag = collegeService.updatecollegeDTO(collegeDTO);

        if (flag){
            return R.ok("修改学院成功");
        } else{
            return R.error("修改学院信息失败");
        }
    }

    //分页查询学院信息（包括学院下的系别）
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());

        String name = null;

        if (params.get("name") != null) {
            name = params.get("name").toString();
        }

        LambdaQueryWrapper<College> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),College::getName,name);

        Page<College> collegePage = new Page<>(page,pageSize);
        Page<CollegeDTO> collegeDTOPage = new Page<>();
        collegePage = collegeService.page(collegePage, lambdaQueryWrapper);

        //获取到分页对象的综合信息，学院包括学院下的系别
        collegeDTOPage = collegeService.getCollegeDtoPage(collegePage);



        R r = R.ok("分页查询成功！");
        r.put("collegePage", collegeDTOPage);

        return r;
    }

    //列表查询学院信息
    @GetMapping("list")
    public R list(@RequestParam Map<String, Object> params){
        String name = null;

        if (params.get("name") != null) {
            name = params.get("name").toString();
        }

        LambdaQueryWrapper<College> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),College::getName,name);
        return R.ok().put("collegeList", collegeService.list(lambdaQueryWrapper));
    }


}
