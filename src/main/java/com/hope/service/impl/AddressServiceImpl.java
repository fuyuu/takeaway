package com.hope.service.impl;

import com.hope.domain.pojo.Address;
import com.hope.domain.pojo.Result;
import com.hope.mapper.AddressMapper;
import com.hope.service.AddressService;
import com.hope.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    @Transactional
    public Result create(Address address) {
        System.out.println("address--create===="+address);
        try {
            Long userId = Long.parseLong(ThreadLocalUtil.get());
            address.setUserId(userId);

            // 处理默认地址逻辑（如果需要）
            if (address.getTop()==1) {
                // 先将该用户其他地址设为非默认
                addressMapper.updateNoTop(userId);
            }

            int rows = addressMapper.create(address);
            if (rows == 0) {
                // 记录警告日志：操作失败但无异常
                log.warn("地址添加失败，无数据受影响，用户ID：{}", userId);
                return Result.fail("地址添加失败");
            }
            // 记录信息日志：操作成功
            log.info("地址添加成功，地址ID：{}，用户ID：{}", address.getId(), userId);
            return Result.ok("地址添加成功");
        } catch (Exception e) {
            // 记录错误日志：捕获异常并打印堆栈
            log.error("添加地址时发生异常，用户ID：{}", ThreadLocalUtil.get(), e);
            return Result.fail("系统异常，添加地址失败");
        }
    }

    @Override
    public Result delete(Long id) {
        System.out.println("delete--delete===="+id);
        try {
            Long userId = Long.parseLong(ThreadLocalUtil.get());
            int rows = addressMapper.delete(id,userId);
            if(rows ==0){
                log.warn("地址删除失败，地址ID：{}，用户ID：{}（可能无权操作）", id, userId);
                return Result.fail("删除失败或无权操作");
            }
            log.info("地址删除成功，地址ID：{}，用户ID：{}", id, userId);
            return Result.ok("删除成功");
        } catch (Exception e) {
            log.error("删除地址时发生异常，地址ID：{}", id, e);
            return Result.fail("系统异常，添加地址失败");
        }
    }

    @Override
    public Result update(Address address) {
        System.out.println("address--update===="+address);
        try {
            Long userId = Long.parseLong(ThreadLocalUtil.get());
            address.setUserId(userId);
            int rows = addressMapper.update(address);
            if (rows == 0) {
                log.warn("地址更新失败，无数据受影响，地址ID:{},用户ID:{}", address.getId(), userId);
                return Result.fail("地址添加失败或无权操作");
            }
            log.info("地址更新成功，地址ID：{}，用户ID：{}", address.getId(), userId);
            return Result.ok("地址更新成功");
        } catch (Exception e) {
            log.error("地址更新时发生异常,地址ID:{},用户ID:{}", address.getId(), ThreadLocalUtil.get(),e);
            return Result.fail("系统异常，地址更新失败或无权操作");
        }
    }

    @Override
    public Result getAddressById(Long id) {
        System.out.println("address--getAddressById===="+id);
        try {
            Long userId = Long.parseLong(ThreadLocalUtil.get());
            Address addressById = addressMapper.getAddressById(id,userId);
            if(addressById==null){
                log.error("地址查询时发生异常,地址ID:{},用户ID:{}", id, userId);
                return Result.fail("没有查询这个地址,地址不存在或无权查看");
            }
            log.info("地址查询时成功,地址ID:{},用户ID:{}", id, userId);
            return Result.ok(addressById);
        } catch (NumberFormatException e) {
            log.error("地址查询时发生异常,地址ID:{},用户ID:{}", id,ThreadLocalUtil.get(),e);
            return Result.fail("系统异常，地址查询失败或无权操作");
        }
    }

    @Override
//    public Result listByUserId() {
    public Result<List<Address>> listByUserId() {
        System.out.println("address-getAddressList");
        System.out.println("listByUserId");
        try {
            Long userId = Long.parseLong(ThreadLocalUtil.get());
            List<Address> addresses = addressMapper.listByUserId(userId);
            if(addresses==null){
                log.error("查询地址列表时发生失败,用户ID:{}", userId);
                return Result.fail("查询地址列表时发生失败");
            }
            log.info("查询地址列表时成功,用户ID:{},地址列表:{}", userId, addresses);
            return Result.ok(addresses);
        } catch (NumberFormatException e) {
            log.error("查询地址列表时发生异常,用户ID:{}", ThreadLocalUtil.get(),e);
            return Result.fail("系统异常，查询地址列表时发生异常或无权操作");
        }
    }

    @Override
    @Transactional
    public Result setTop(Long id) {
        System.out.println("address--update==id=="+id);
        try {
            Long userId = Long.parseLong(ThreadLocalUtil.get());
            // 先将用户所有地址设为非默认
            addressMapper.updateNoTop(userId);
            // 再将当前地址设为默认
            int rows=addressMapper.setTop(id, userId);
            if(rows==0){
                log.error("设置默认地址异常，地址ID：{},用户ID:{}", id,userId);
                return Result.fail("设置默认地址失败");
            }
            log.info("查询地址列表时成功,用户ID:{},用户ID:{}", userId, userId);
            return Result.ok("设置默认地址成功");
        } catch (Exception e) {
            log.error("设置默认地址异常，地址ID：{}", id, e);
            return Result.fail("系统异常，设置默认地址失败");
        }
    }
}
