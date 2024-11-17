package com.shixin.business.service;

import com.shixin.business.domain.Staffinfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.shixin.business.domain.Dorm;

import java.util.List;

public interface DormServiceI {
	Page<Dorm> findDorms(Pageable pageable);
	
	Boolean importExcel(MultipartFile file);

	List<Dorm> getDorms(Staffinfo staffinfo);
}
