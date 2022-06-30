package com.erahub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.common.error.system.SystemCodeEnum;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.model.system.Log;
import com.erahub.common.vo.system.LogVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.system.mapper.LogMapper;
import com.erahub.system.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/4/2 20:24
 * @Version 1.0
 **/
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    /**
     * 保存登入日志
     * @param log
     */
    @Override
    public void saveLog(Log log) {
        logMapper.insert(log);
    }


    /**
     * 删除操作日志
     * @param id
     */
    @Override
    public void delete(Long id) throws SystemException {
        Log log = logMapper.selectById(id);
        if(log==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"要删除的操作日志不存在");
        }
        logMapper.deleteById(id);
    }


    @Override
    public PageVO<LogVO> findLogList(Integer pageNum, Integer pageSize, LogVO logVO) {
        IPage<Log> loginLogIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Log> loginLogQueryWrapper = new QueryWrapper<>();
        loginLogQueryWrapper.orderByDesc("create_time");
        if(logVO.getLocation()!=null&&!"".equals(logVO.getLocation())){
            loginLogQueryWrapper.like("location",logVO.getLocation());
        }
        if(logVO.getIp()!=null&&!"".equals(logVO.getIp())){
            loginLogQueryWrapper.like("ip",logVO.getIp());
        }
        if(logVO.getUsername()!=null&&!"".equals(logVO.getUsername())){
            loginLogQueryWrapper.like("username",logVO.getUsername());
        }
        loginLogIPage = logMapper.selectPage(loginLogIPage, loginLogQueryWrapper);
        List<Log> loginLogs = loginLogIPage.getRecords();
        List<LogVO> logVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(loginLogs)){
            for (Log loginLog : loginLogs) {
                LogVO logVO1 = new LogVO();
                BeanUtils.copyProperties(loginLog,logVO1);
                logVOS.add(logVO1);
            }
        }

        return new PageVO<>(loginLogIPage.getTotal(),logVOS);
    }

    /**
     * 批量删除
     * @param list
     */
    @Override
    public void batchDelete(List<Long> list) throws SystemException {
        for (Long id : list) {
            Log log = logMapper.selectById(id);
            if(log==null){
                throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"id="+id+",操作日志不存在");
            }
            delete(id);
        }
    }
}
