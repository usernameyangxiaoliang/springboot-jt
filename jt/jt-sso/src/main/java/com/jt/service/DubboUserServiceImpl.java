package com.jt.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import redis.clients.jedis.JedisCluster;
@Service
public class DubboUserServiceImpl implements DubboUserService{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;

	@Override
	@Transactional
	public void doRegister(User user) {
		//将密码加密
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password).setEmail(user.getPhone()).setCreated(new Date()).setUpdated(user.getCreated());
		userMapper.insert(user);
		
	}
	/**
	 * 1.检验用户名或密码是否正确
	 * 2.如果数据不正确返回null
	 * 3.如果数据正确
	 *   1.生成加密秘钥：MD5(JT_TICKECT+username+当前的毫秒数);
	 *   2.将userDB数据转为userJSON
	 *   3.将数据保存到redis中7天超时
	 * 4.返回token  
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		// 1.将密码加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
				user.setPassword(md5Pass);
				QueryWrapper<User> queryWrapper = 
									new QueryWrapper<User>(user);
				User userDB = userMapper.selectOne(queryWrapper);
				//2.判断数据正确执行下列代码
				if(userDB != null) {
					token = "JT_TICKET_"+userDB.getUsername()
							+ System.currentTimeMillis();
					token = DigestUtils
							.md5DigestAsHex(token.getBytes());
					
					//脱敏处理  一切从业务出发.
					userDB.setPassword("你猜猜!!!!");
					String userJSON = ObjectMapperUtil.toJSON(userDB);
					//将数据保存到redis中
					jedisCluster.setex(token,7*24*3600, userJSON);
				}
				return token;
			}
}