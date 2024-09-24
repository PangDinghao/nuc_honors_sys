package com.nuc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.sys.common.R;
import com.nuc.sys.pojo.Honor;
import com.nuc.sys.pojo.HonorDTO;
import com.nuc.sys.pojo.User;
import com.nuc.sys.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/honor")
public class HonorController {

    @Autowired
    private HonorService honorService;
    @Autowired
    private HttpSession session;

    //学生教师申请荣誉奖项
    @PostMapping("/add")
    public R add(@RequestBody Honor honor){
        User currentUser = (User)session.getAttribute("currentUser");
        honor.setCreateUserid(currentUser.getId());

        boolean flag = honorService.save(honor);

        if(flag){
            return R.ok("申请成功");
        }else{
            return R.error("申请失败");
        }
    }

    //删除荣誉奖项
    @GetMapping("/delete")
    public R delete(@RequestParam("ids") List<Integer> ids){
        boolean flag = honorService.removeByIds(ids);

        if(flag){
            return R.ok("删除成功");
        }else{
            return R.error("删除失败");
        }
    }

    //修改荣誉信息，或审核信息
    @PostMapping("/update")
    public R update(@RequestBody Honor honor){
        User currentUser = (User)session.getAttribute("currentUser");
        honor.setUpdateUserid(currentUser.getId());

        boolean flag = honorService.updateById(honor);

        if(flag){
            return R.ok("修改成功");
        }else{
            return R.error("修改失败");
        }
    }



    //分页查询荣誉信息
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());

        //查询条件
        //荣誉奖项姓名
        String name = null;
        //荣誉类型
        int typeId = 0;
        //审核状态
        int status = 0;

        LambdaQueryWrapper<Honor> queryWrapper = new LambdaQueryWrapper<>();

        if (params.get("name")!= null){
            name = params.get("name").toString();
            queryWrapper.like(Honor::getName,name);
        }
        if (params.get("typeId")!= null){
            typeId = Integer.parseInt(params.get("typeId").toString());
            queryWrapper.eq(Honor::getTypeId,typeId);
        }
        if (params.get("status")!= null){
            status = Integer.parseInt(params.get("status").toString());
            queryWrapper.eq(Honor::getStatus,status);
        }

        Page<Honor> honorPage = new Page<>(page,pageSize);
        Page<HonorDTO> honorDtoPage = new Page<>(page,pageSize);

        honorPage = honorService.page(honorPage, queryWrapper);
        honorDtoPage = honorService.getHonorDtoPage(honorPage);

        return R.ok().put("honorPage",honorDtoPage);

    }

    //获取单个荣誉信息
    @GetMapping("/get")
    public R get(Integer id){
        HonorDTO honorDTO = honorService.getHonorDtoById(id);
        return R.ok().put("honorDTO",honorDTO);
    }


}
