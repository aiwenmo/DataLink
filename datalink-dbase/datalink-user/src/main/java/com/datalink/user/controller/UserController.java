package com.datalink.user.controller;

import com.datalink.base.annotation.LoginUser;
import com.datalink.base.model.*;
import com.datalink.log.AuditLog;
import com.datalink.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.slf4j.Slf4j;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 Controller
 *
 * @author wenmo
 * @since 2021-05-03
 */
@Slf4j
@Api(tags = "用户模块api")
@RestController
public class UserController {
    private static final String ADMIN_CHANGE_MSG = "超级管理员不给予修改";
    private static final Integer ADMIN_CODE = 1;

    @Autowired
    private UserService userService;

    /**
     * 动态新增修改用户
     */
    @ApiOperation(value = "动态新增修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "nickname", value = "姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "headUrl", value = "头像Url", required = false, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sex", value = "性别", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "enabled", value = "是否启用", required = true, dataType = "Boolean"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", required = false, dataType = "Date"),
            @ApiImplicitParam(name = "updateTime", value = "更新时间", required = false, dataType = "Date")
    })
    @CacheEvict(value = "user", key = "#user.username")
    @AuditLog(operation = "'新增或更新用户:' + #sysUser.username")
    @PutMapping("/users")
    public Result saveOrUpdate(@RequestBody User user) throws Exception {
        if(user.getId()!=null&&checkAdmin(user.getId())){
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        return userService.saveOrUpdateUser(user);
    }

    /**
     * 动态查询用户列表
     */
    @ApiOperation(value = "动态查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "页记录数", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String"),
            @ApiImplicitParam(name = "filter", value = "排序值", required = false, dataType = "String"),
    })
    @PostMapping("/users")
    public ProTableResult<User> listUsers(@RequestBody JsonNode para) {
        return userService.selectForProTable(para);
    }

    /**
     * 批量删除用户
     */
    @ApiOperation(value = "批量删除用户")
    @DeleteMapping(value = "/users")
    public Result deleteMul(@RequestBody JsonNode para) {
        if (para.size()>0){
            boolean isAdmin = false;
            List<Integer> error = new ArrayList<>();
            for (final JsonNode item : para){
                Integer id = item.asInt();
                if(checkAdmin(id)){
                    isAdmin = true;
                    error.add(id);
                    continue;
                }
                if(!userService.removeById(id)){
                    error.add(id);
                }
            }
            if(error.size()==0&&!isAdmin) {
                return Result.succeed("删除成功");
            }else if(isAdmin) {
                return Result.succeed("删除部分成功，但"+error.toString()+"删除失败，共"+error.size()+"次失败，其中"+ADMIN_CHANGE_MSG+"。");
            }else {
                return Result.succeed("删除部分成功，但"+error.toString()+"删除失败，共"+error.size()+"次失败。");
            }
        }else{
            return Result.failed("请选择要删除的记录");
        }
    }

    /**
     * 获取指定ID的用户信息
     */
    @ApiOperation(value = "获取指定ID的用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer")
    })
    @PostMapping("/getOneById")
    public Result getOneById(Integer id) {
        User user = userService.getById(id);
        return Result.succeed(user, "获取成功");
    }

    /**
     * 当前登录用户 LoginAppUser
     *
     * @return
     */
    @ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/users/current")
    public Result<LoginAppUser> getLoginAppUser(@LoginUser User user) {
        return Result.succeed(userService.findByUsername(user.getUsername()));
    }

    /**
     * 查询用户实体对象SysUser
     */
    @GetMapping(value = "/users/name", params = "username")
    @ApiOperation(value = "根据用户名查询用户实体")
    @Cacheable(value = "user", key = "#username")
    public User selectByUsername(String username) {
        return userService.selectByUsername(username);
    }


    /**
     * 查询用户登录对象LoginAppUser
     */
    @GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    public LoginAppUser findByUsername(String username) {
        return userService.findByUsername(username);
    }

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    @ApiOperation(value = "根据手机号查询用户")
    public User findByMobile(String mobile) {
        return userService.findByMobile(mobile);
    }

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    @GetMapping(value = "/users-anon/openId", params = "openId")
    @ApiOperation(value = "根据OpenId查询用户")
    public User findByOpenId(String openId) {
        return userService.findByOpenId(openId);
    }

    /**
     * 是否超级管理员
     */
    private boolean checkAdmin(Integer id) {
        return ADMIN_CODE.equals(id);
    }
}

