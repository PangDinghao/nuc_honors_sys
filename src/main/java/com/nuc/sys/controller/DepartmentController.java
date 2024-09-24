package com.nuc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.sys.common.R;
import com.nuc.sys.pojo.Department;
import com.nuc.sys.pojo.DepartmentDTO;
import com.nuc.sys.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PostMapping("/add")
    public R add(@RequestBody Department department){
        boolean flag = departmentService.save(department);

        if (flag){
            return R.ok("新增成功");
        }else {
            return R.error("新增失败");
        }
    }

    @GetMapping("/delete")
    public R delete(@RequestParam("ids") List<Integer> ids){
        boolean flag = departmentService.removeBatchByIds(ids);
        if (flag){
            return R.ok("删除成功");
        }else {
            return R.error("删除失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody Department department) {
        boolean flag = departmentService.updateById(department);
        if (flag) {
            return R.ok("更新成功");
        } else {
            return R.error("更新失败");
        }
    }


    //根据学院查询系别
    //如果传递进来的是0，则查询所有系别
    //如果是1，则查询学院对应的系别
    //如果是-1，则查询学院id为0的系别
    @GetMapping("/list/{collegeId}/{name}")
    public R list(@PathVariable("collegeId") Integer collegeId, @PathVariable("name") String name) {
        if (collegeId == 0) {
            return R.ok().put("departmentList", departmentService.list());
        }
        if (collegeId == -1){
            return R.ok().put("departmentList", departmentService
                    .list(new LambdaQueryWrapper<Department>()
                            .eq(Department::getCollegeId, 0)));
        }

        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getCollegeId, collegeId);

        return R.ok().put("departmentList", departmentService.list(lambdaQueryWrapper));
    }

    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());

        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //学院名字
        if (params.get("name") != null) {
            String name = params.get("name").toString();
            lambdaQueryWrapper.like(Department::getName, name);
        }



        Page<Department> departmentPage = departmentService.page(new Page<>(page, pageSize), lambdaQueryWrapper);

        Page<DepartmentDTO> departmentDTOPage = departmentService.getDepartmentDTOPage(departmentPage);


        return R.ok().put("departmentPage", departmentDTOPage);
    }

}
