package com.datalink.user.service;

import com.datalink.base.model.LoginAppUser;
import com.datalink.base.model.Result;
import com.datalink.base.model.User;
import com.datalink.db.mybatis.service.ISuperService;

/**
 * 服务类
 *
 * @author wenmo
 * @since 2021-05-03
 */
public interface UserService extends ISuperService<User> {

    boolean saveUser(User user) throws Exception;

    Result saveOrUpdateUser(User user) throws Exception;

    /**
     * 获取UserDetails对象
     * @param username
     * @return
     */
    LoginAppUser findByUsername(String username);

    LoginAppUser findByMobile(String username);

    LoginAppUser findByOpenId(String username);
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User selectByUsername(String username);
    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    User selectByMobile(String mobile);
    /**
     * 根据openId查询用户
     * @param openId
     * @return
     */
    User selectByOpenId(String openId);
    /**
     * 通过SysUser 转换为 LoginAppUser，把roles和permissions也查询出来
     * @param sysUser
     * @return
     */
    LoginAppUser getLoginAppUser(User sysUser);
}
