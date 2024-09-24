package com.nuc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.sys.common.R;
import com.nuc.sys.pojo.User;
import com.nuc.sys.pojo.UserDTO;
import com.nuc.sys.service.UserService;
import com.nuc.sys.utils.ExcelUtils;
import com.nuc.sys.utils.JavaMailUntil;
import com.nuc.sys.utils.MD5Utils;
import com.nuc.sys.utils.VerificationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @description 用户
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private HttpSession session;

    //登录
    @PostMapping("/login")
    public R login(User user) {
        //账号密码判空
        if (user.getEmail() == "" || user.getPassword() == "") {
            return R.error("用户名或密码不能为空");
        }

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, user.getEmail())
                .eq(User::getPassword, user.getPassword());

        User result = userService.getOne(lambdaQueryWrapper);


        //没有用户信息，登录失败
        if (result == null) {
            return R.error("邮箱或密码错误");
        }

        //登录成功，将用户信息存入session
        session.setAttribute("currentUser", result);

        R r = R.ok("登录成功！");
        r.put("currentUser", result);
        return r;
    }

    //注册邮箱验证码
    @PostMapping("/email")
    public R email(User user) throws MessagingException, InterruptedException {
        //账号密码判空
        if (user.getEmail() == "" ) {
            return R.error("请输入邮箱");
        }


        //生成六位数验证码
        String code = VerificationCodeGenerator.generateVerificationCode(6);

        //	创建Session会话
        Session session = JavaMailUntil.createSession();

        //不是上边的session，将验证码和邮箱存到session中
        this.session.setAttribute(user.getEmail(), code);

        //	创建邮件对象
        MimeMessage message = new MimeMessage(session);
        message.setSubject("欢迎注册中北大学学院荣誉奖项管理系统！验证码:【" + code + "】");
        message.setText("您的验证码为：【"+code+"】,请妥善保管您的验证码，不要给他人使用。");
        //发送方邮箱账号
        message.setFrom(new InternetAddress("535957534@qq.com"));
        //接收方邮箱账号
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(user.getEmail()));

        //	发送
        Transport.send(message);

        return R.ok("验证码发送成功");

    }

    //注册
    @PostMapping("/register")
    public R register(@RequestBody Map map) {
        String eamil;
        String password;
        User user = new User();
        String code;

        //判断邮箱和密码是否为空
        if(map.get("email") != null && map.get("password") != null){
            //获取邮箱和密码
            eamil = map.get("email").toString();
            password = map.get("password").toString();
            user.setEmail(eamil);
            user.setPassword(password);
        }else {
            return R.error("邮箱和密码不能为空");
        }

        //判断验证码是否为空
        if(map.get("code") == null){
            return R.error("请输入验证码");
        }else {
            //获取输入的验证码
            code = map.get("code").toString();
        }

        String session_code = null;
        //根据email获取到session中存储的code验证码
        if (session.getAttribute(user.getEmail()) != null){
            session_code = session.getAttribute(user.getEmail()).toString();
        }else {
            return R.error("验证码已失效，请重新获取");
        }

        boolean flag = false;
        //判断验证码是否一致
        if(session_code.equals(code)){
            //验证码一致，注册用户

            //生成默认用户名
            user.setName("用户" + VerificationCodeGenerator.generateVerificationCode(6));
            //添加用户
            flag = userService.save(user);
            //删掉session中存储的验证码信息
            session.removeAttribute(user.getEmail());
        }else {
            //验证码错误
            return R.error("验证码错误");
        }

        if (flag){
            return R.ok("注册成功");
        }else {
            return R.error("注册失败");
        }

    }

    //退出登录
    @GetMapping("/logout")
    public R logout(HttpSession session) {
        session.invalidate();
        return R.ok("退出成功");
    }

    //列表查询
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        int collegeId = 0;
        int stuNumber = 0;
        String email = null;
        int departmentId = 0;
        int roleId = 0;

        //学院、学/工号、邮箱、专业
        if (params.get("collegeId") != null) {
            collegeId = Integer.parseInt(params.get("collegeId").toString());
        }
        if (params.get("stuNumber") != null) {
            stuNumber = Integer.parseInt(params.get("stuNumber").toString());
        }
        if (params.get("email") != null) {
            email = params.get("email").toString();
        }
        if (params.get("departmentId") != null) {
            departmentId = Integer.parseInt(params.get("departmentId").toString());
        }
        if (params.get("roleId") != null) {
            roleId = Integer.parseInt(params.get("roleId").toString());
        }

        //条件构造器
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(collegeId!= 0 && !Objects.isNull(collegeId),User::getCollegeId, collegeId);
        wrapper.like(stuNumber!= 0 && !Objects.isNull(stuNumber),User::getStuNumber, stuNumber);
        wrapper.like(StringUtils.isNotEmpty(email),User::getEmail, email);
        wrapper.eq(departmentId!= 0 && !Objects.isNull(departmentId),User::getDepartmentId, departmentId);
        wrapper.eq(roleId != 0 && !Objects.isNull(roleId),User::getRoleId, roleId);



        List<User> list = userService.list(wrapper);

        return R.ok().put("list", list);
    }
    //分页查询
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {

        //分页查询
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int page = Integer.parseInt(params.get("page").toString());


        int collegeId = 0;
        int stuNumber = 0;
        String email = null;
        int departmentId = 0;
        int roleId = 0;

        //学院、学/工号、邮箱、专业、角色
        if (params.get("collegeId") != null) {
            collegeId = Integer.parseInt(params.get("collegeId").toString());
        }
        if (params.get("stuNumber") != null) {
            stuNumber = Integer.parseInt(params.get("stuNumber").toString());
        }
        if (params.get("email") != null) {
            email = params.get("email").toString();
        }
        if (params.get("departmentId") != null) {
            departmentId = Integer.parseInt(params.get("departmentId").toString());
        }
        if (params.get("roleId") != null) {
            roleId = Integer.parseInt(params.get("roleId").toString());
        }

        //条件构造器
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(collegeId!= 0 && !Objects.isNull(collegeId),User::getCollegeId, collegeId);
        wrapper.like(stuNumber!= 0 && !Objects.isNull(stuNumber),User::getStuNumber, stuNumber);
        wrapper.like(StringUtils.isNotEmpty(email),User::getEmail, email);
        wrapper.eq(departmentId!= 0 && !Objects.isNull(departmentId),User::getDepartmentId, departmentId);
        wrapper.eq(roleId != 0 && !Objects.isNull(roleId),User::getRoleId, roleId);

        //分页构造器
        Page<User> pageInfo = new Page<>(page, pageSize);

        Page<UserDTO> userDtoPage = new Page<>();

        Page<User> pageUser = userService.page(pageInfo, wrapper);

        //获取到分页对象的真实信息
        userDtoPage = userService.getUserDtoPage(pageUser);

        R r = R.ok("分页查询成功！");
        r.put("pageUser", userDtoPage);

        return r;
    }

    //新增用户(管理员功能)
    @PostMapping("/add")
    public R add(@RequestBody User user) {


        //判断账号是否存在
        User add_user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail()));
        if (add_user != null) {
            return R.error("新增失败！账号已存在");
        }


        //如果没有指定用户名则生成默认用户名
        if(user.getName() == null ||user.getName().equals("")){
            user.setName("用户" + VerificationCodeGenerator.generateVerificationCode(6));
        }

        boolean save = userService.save(user);
        if (save) {
            return R.ok("新增成功");
        }
        return R.error("新增失败");
    }

    //修改用户
    @PostMapping("/update")
    public R update(@RequestBody User user) {

        boolean update = userService.updateById(user);
        if (update) {
            return R.ok("修改成功");
        }
        return R.error("修改失败");
    }

    //删除用户
    @GetMapping("/delete")
    public R delete(@RequestParam("ids") List<Integer> ids) {
        boolean remove = userService.removeBatchByIds(ids);

        if (remove) {
            return R.ok("删除成功");
        }
        return R.error("删除失败");
    }

    //获取用户
    @GetMapping("/get")
    public R get(Integer id) {
        UserDTO user = userService.getDtoById(id);
        if (user != null) {
            return R.ok().put("user", user);
        }
        return R.error("查询失败！");
    }

    //导出所有用户Excel
    @GetMapping("/export")
    @ResponseBody
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<User> list = userService.list();

        //转换成DTO
        List<UserDTO> userDtoList = userService.getUserDtoList(list);

        // 表头数据
        List<Object> head = Arrays.asList("姓名","年龄","性别","联系电话","邮箱","密码","学院","专业","角色");

        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        for (UserDTO userDTO : userDtoList) {
            //用户数据
            List<Object> row = new ArrayList<>();

            row.add(userDTO.getName());
            row.add(userDTO.getAge());
            row.add(userDTO.getSex());
            row.add(userDTO.getPhone());
            row.add(userDTO.getEmail());
            row.add(userDTO.getPassword());
            row.add(userDTO.getCollegeName());
            row.add(userDTO.getDepartmentName());
            row.add(userDTO.getRoleName());

            sheetDataList.add(row);
        }

        // 导出数据
        ExcelUtils.export(response,"用户表", sheetDataList);

        return R.ok();
    }

    //导入用户Excel(管理员功能)
    @PostMapping("/import")
    public R importUser(@RequestPart("file") MultipartFile file) throws Exception {

        List<UserDTO> users = ExcelUtils.readMultipartFile(file, UserDTO.class);

        //获取DTO中的信息，并查询数据库，获取id,并保存到数据库
        boolean b = userService.SaveImportUser(users);

        if (!b){
            return R.error("导入失败！");
        }
        R r = R.ok("导入成功！");

        return r;
    }
}