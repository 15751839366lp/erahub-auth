package com.erahub.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erahub.common.model.system.LoginLog;
import com.erahub.common.vo.system.UserVO;


import java.util.List;
import java.util.Map;

/**
 * @Author lipeng
 * @Date 2020/3/20 19:09
 * @Version 1.0
 **/
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /**
     * 用户登入报表
     * @param userVO
     * @return
     */
    List<Map<String,Object>> userLoginReport(UserVO userVO);

}
