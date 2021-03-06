package org.lanqiao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.lanqiao.dao.MemberDao;
import org.lanqiao.pojo.Member;
import org.lanqiao.service.MemberService;
import org.lanqiao.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;


    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    // 保存会员信息
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            // 使用MD5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }


    //根据月份查询会员数量
    public List<Integer> findMemberCountByMonths(List<String> months) {//2018.05
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            String date = month + ".31";//2018.05.31
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }
        return memberCount;
    }

    // 通过手机号更新会员信息
    public void updateMemberByPhone(Member member) {
        memberDao.updateMemberByPhone(member);
    }


}
