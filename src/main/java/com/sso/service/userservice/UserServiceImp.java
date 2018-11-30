package com.sso.service.userservice;

import com.alibaba.dubbo.config.annotation.Service;
import com.sso.service.dao.UserMapper;
import org.project.service.UserService;
import org.projectshop.pojo.User;
import org.projectshop.pojo.result.Result;
import org.projectshop.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "1.0.0")
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public Result getToken(User user) {
        return null;
    }

    @Override
    public Result isLogin() {
        return null;
    }

    @Override
    public Result checkUserName(String s) {
        Result result = null;
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(s);
        java.util.List<User> list = userMapper.selectByExample(userExample);
        if(list.size() > 0) {
            result = new Result(10001,"true",list);
            return result;
        }else {
            result = new Result(10002,"false",null);
            return result;
        }
    }

    @Override
    public Result login(String s, String s1) {
        return null;
    }

    @Override
    public Result logout(String s) {
        return null;
    }
}
