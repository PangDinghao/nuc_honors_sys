package com.nuc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.sys.common.R;
import com.nuc.sys.pojo.New;
import com.nuc.sys.pojo.User;
import com.nuc.sys.service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/new")
public class NewController {
    @Autowired
    private NewService newService;

    @Autowired
    HttpSession session;

    @PostMapping("/add")
    public R add(@RequestBody New news){
        //获取当前登录用户
        User user = (User) session.getAttribute("currentUser");

        news.setCreateUserid(user.getId());
        news.setCreateDatatime(LocalDateTime.now());

        boolean flag = newService.save(news);
        if(flag){
            return R.ok("添加成功");
        }else{
            return R.error("添加失败");
        }

    }

    @GetMapping("/delete")
    public R delete(@RequestParam("ids") List<Integer> ids){
        boolean flag = newService.removeBatchByIds(ids);
        if(flag){
            return R.ok("删除成功");
        }else{
            return R.error("删除失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody New news){
        boolean flag = newService.updateById(news);
        if(flag){
            return R.ok("修改成功");
        }else{
            return R.error("修改失败");
        }
    }

    //查询全部新闻
    @GetMapping ("/list")
    public R list(@RequestParam Map<String, Object> params){

        String title = null;
        int type = 0;

        if (params.get("title") != null) {
            title = params.get("title").toString();
        }

        if (params.get("type") != null) {
            type = Integer.parseInt(params.get("type").toString());
        }

        LambdaQueryWrapper<New> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(title),New::getTitle,title);
        lambdaQueryWrapper.eq(type!= 0,New::getType,type);

        List<New> list = newService.list(lambdaQueryWrapper);
        return R.ok("查询成功").put("newList",list);
    }

    //分页查询
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());

        String title = null;
        int type = 0;

        if (params.get("title") != null) {
            title = params.get("title").toString();
        }

        if (params.get("type") != null) {
            type = Integer.parseInt(params.get("type").toString());
        }

        LambdaQueryWrapper<New> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(title),New::getTitle,title);
        lambdaQueryWrapper.eq(type!= 0,New::getType,type);

        Page<New> newPage = new Page<>(page,pageSize);
        Page<New> newPageInfo = newService.page(newPage,lambdaQueryWrapper);

        return R.ok("查询成功").put("newPage",newPageInfo);

    }

    //获取单个新闻
    @GetMapping("/get")
    public R get(Integer id) {
        New news = newService.getById(id);
        int hit = news.getHit();
        news.setHit(++hit);
        newService.updateById(news);

        return R.ok("查询成功").put("new",news);
    }




}
