package com.shixin.business.service;


import com.shixin.business.domain.External;
import com.shixin.business.domain.Staffinfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ExternalServiceI {
    void saveExternal(External external, Staffinfo staffinfo);

    Page<External> externalList(Pageable pageable, Staffinfo staffinfo);

    void overVisit(String id);

    Page<External> externalHistoryList(Pageable pageable, Staffinfo staffinfo, String stuname, String startTime, String endTime);

    List<External> findHistoryList(Staffinfo staffinfo);
}
