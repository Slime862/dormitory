package com.shixin.business.service;

import com.shixin.business.domain.User;
import com.shixin.business.domain.UserExpand;

public interface LoginServiceI {
	UserExpand getUserLoginInfo(User user);
}
