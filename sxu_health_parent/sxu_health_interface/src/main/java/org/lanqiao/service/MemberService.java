package org.lanqiao.service;

import org.lanqiao.pojo.Member;

import java.util.List;

public interface MemberService {

    // 根据手机号来查询会员
    public Member findByTelephone(String telephone);
    // 添加会员
    public void add(Member member);
    public List<Integer> findMemberCountByMonths(List<String> months);

    public void updateMemberByPhone(Member member);
}
