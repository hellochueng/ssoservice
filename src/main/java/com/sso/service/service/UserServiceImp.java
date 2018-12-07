package com.sso.service.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.project.util.CommonUtils;
import com.sso.service.common.LoginSymbol;
import com.sso.service.dao.UserMapper;
import com.sso.service.jedis.RedisHandle;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.project.service.UserService;
import org.projectshop.pojo.User;
import org.projectshop.pojo.result.Result;
import org.projectshop.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service(version = "1.0.0")
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisHandle redisHandle;

    /**
     * 通过token获取用户信息
     * @param user
     * @return
     */
    @Override
    public Result getToken(User user) {
        return null;
    }

    /**
     * 判断是否登录
     * @return boolean
     */
    @Override
    public Result isLogin(String token,String nonce,String signature) {
        //获取secretKey
        String secretKey = (String)redisHandle.get(LoginSymbol.AUTH_TOKEN_SECRET_KEY_PREFIX + token);

        if(CommonUtils.isEmpty(secretKey)){

            return new Result(10002,"验证信息不存在",null);
        }else{

            //生成口令hash
            String hash = DigestUtils.md5Hex("token"+token+"nonce"+nonce);

            //判断是否对应口令
            if(StringUtils.equals(signature,hash)){

                //获取当前用户
                User user = (User)redisHandle.get(LoginSymbol.SECRET_KEY_LOGIN_SUBJECT_PREFIX + secretKey);

                return new Result(10001,"success",user);
            }else{

                return new Result(10002,"签名验证失败",null);
            }
        }
    }

    /**
     * 判断名字是否存在
     * @param s
     * @return Result
     */
    @Override
    public Result checkUserName(String s) {

        UserExample userExample = new UserExample();

        UserExample.Criteria criteria = userExample.createCriteria();

        criteria.andUsernameEqualTo(s);

        java.util.List<User> list = userMapper.selectByExample(userExample);

        if(list.size() > 0) {

            return new Result(10001,"true",list);
        }else {

            return new Result(10002,"false",null);
        }
    }

    /**
     * 登录方法
     * @param s
     * @param s1
     * @return Result
     */
    @Override
    public Result login(String s, String s1) {

        User user = userMapper.findOnlyOne(s);

        //判断当前用户名是否存在结果
        if(CommonUtils.isNotEmpty(user)){

            //判断用户是否激活
            if(user.isValid()) {

                //判断密码错误次数
                if ((user.getErrorCount() == null ? 0 : user.getErrorCount()) > 5) {

                    return new Result(10002, "密码错误次数过多，请修改密码", null);
                } else {
                    //判断输入密码是否正确
                    if (user.getPassword().equals(s1)) {

                        //密码正确将用户信息进行存储
                        Map<String, Object> map = getTokenByLogin(user);

                        //密码制空
                        user.setPassword(null);
                        map.put("user", user);

                        //返回结果
                        return new Result(10001, "success", map);
                    } else {

                        //添加密码错误次数
                        user.setErrorCount(user.getErrorCount() + 1);

                        userMapper.updateByPrimaryKey(user);

                        return new Result(10002, "密码错误，你还有" + (6 - user.getErrorCount()) + "次机会", null);
                    }
                }
            }else{
                return new Result(10002, "用户未激活", null);
            }
        }else{
            return new Result(10002,"用户名不存在",null);
        }

    }

    /**
     * 登出功能
     * @param s
     * @return
     */
    @Override
    public Result logout(String s) {
        //获取secretKey
        String secretKey = (String)redisHandle.get(LoginSymbol.AUTH_TOKEN_SECRET_KEY_PREFIX + s);

        //删除redis中对应的secretKey
        redisHandle.remove(LoginSymbol.AUTH_TOKEN_SECRET_KEY_PREFIX + s);

        //删除redis中对应的user
        redisHandle.remove(LoginSymbol.SECRET_KEY_LOGIN_SUBJECT_PREFIX + secretKey);

        return new Result(10001,"success",null);
    }

    @Override
    public Result register(User user) {

        //判断用户是否存在
        User registerUser = userMapper.findOnlyOne(user.getUserName());

        if(CommonUtils.isNotEmpty(registerUser)){
            return new Result(10002,"用户名已存在",null);
        } else {

        }

        return null;
    }


    /**
     * 在redis中生成token等信息
     * @param user 用户信息
     * @return
     */
    public Map<String, Object> getTokenByLogin(User user) {

        //生成secretKey 和 authToken
        String secretKey = RandomStringUtils.random(32, true, true);

        String authToken = RandomStringUtils.random(32, true, true);


        //将token信息和用户信息存入到 redis的缓存中
        redisHandle.set(LoginSymbol.AUTH_TOKEN_SECRET_KEY_PREFIX + authToken, secretKey);
        redisHandle.setExpireTime(LoginSymbol.AUTH_TOKEN_SECRET_KEY_PREFIX + authToken, 604800L);

        redisHandle.set(LoginSymbol.SECRET_KEY_LOGIN_SUBJECT_PREFIX + secretKey, user);
        redisHandle.setExpireTime(LoginSymbol.SECRET_KEY_LOGIN_SUBJECT_PREFIX + secretKey, 604800L);

        Map result = new HashMap();

        result.put("authToken", authToken);
        result.put("secretKey", secretKey);

        return result;
    }

}
