package com.shixin.business.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shixin.business.domain.Staffinfo;
import com.shixin.business.domain.Stuinfo;
import com.shixin.business.domain.User;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.repository.UserRepository;
import com.shixin.business.service.LoginServiceI;
import com.shixin.business.service.StaffServiceI;
import com.shixin.business.service.StuServiceI;

@Service
public class LoginServiceImpl implements LoginServiceI {

    @Autowired
    private UserRepository loginRepository;
    @Autowired
    private StuServiceI stuServiceI;
    @Autowired
    private StaffServiceI staffServiceI;

    @Override
    public UserExpand getUserLoginInfo(User user) {
        User info = loginRepository.findByUsernameAndPasswordAndPermission(user.getUsername(), user.getPassword(),
                user.getPermission());
        UserExpand userExpand = null;
        if (null != info) {
            userExpand = new UserExpand();
            switch (info.getPermission()) {
                case 0:
                    Stuinfo stu = stuServiceI.findById(user.getUsername());
                    BeanUtils.copyProperties(info, userExpand);
                    userExpand.setName(stu.getName());
                    userExpand.setStuinfo(stu);
                    break;
                case 1:
                    Staffinfo staffinfo = staffServiceI.findById(user.getUsername());
                    BeanUtils.copyProperties(info, userExpand);
                    userExpand.setName(staffinfo.getStaffname());
                    userExpand.setStaffinfo(staffinfo);
                    break;
                default:
                    userExpand.setName(info.getUsername()).setUsername(info.getUsername()).setId(info.getUsername()).setPermission(info.getPermission());
                    break;
            }
        }
        return userExpand;
    }

}
