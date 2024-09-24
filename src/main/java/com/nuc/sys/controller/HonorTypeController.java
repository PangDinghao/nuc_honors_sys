package com.nuc.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.sys.common.R;
import com.nuc.sys.pojo.HonorType;
import com.nuc.sys.service.HonorService;
import com.nuc.sys.service.HonorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/honorType")
public class HonorTypeController {
    @Autowired
    private HonorTypeService honorTypeService;

    @PostMapping("/add")
    public R add(@RequestBody HonorType honorType) {
        boolean flag = honorTypeService.save(honorType);
        if (flag) {
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @GetMapping("/delete")
    public R delete(@RequestParam("ids") List<Integer> ids){
        boolean flag = honorTypeService.removeByIds(ids);
        if (flag) {
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody HonorType honorType) {
        boolean flag = honorTypeService.updateById(honorType);
        if (flag) {
            return R.ok("更新成功");
        } else {
            return R.error("更新失败");
        }
    }



    @GetMapping("/list")
    public R list() {
        return R.ok("获取成功").put("honorTypeList", honorTypeService.list());
    }

    @GetMapping("page")

    public R page(Integer page, Integer pageSize) {
        Page<HonorType> pageParam = new Page<>(page, pageSize);

        return R.ok("获取成功").put("honorTypePage", honorTypeService.page(pageParam));
    }

}
