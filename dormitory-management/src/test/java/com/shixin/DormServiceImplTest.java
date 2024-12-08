package com.shixin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.shixin.business.domain.Dorm;
import com.shixin.business.domain.Staffinfo;
import com.shixin.business.repository.DormRepository;
import com.shixin.business.service.impl.DormServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

public class DormServiceImplTest {

    @Mock
    private DormRepository dormRepository;

    @InjectMocks
    private DormServiceImpl dormService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindDorms() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Dorm> dorms = Arrays.asList(new Dorm(), new Dorm());
        Page<Dorm> expectedPage = new PageImpl<>(dorms, pageable, dorms.size());
        when(dormRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Dorm> result = dormService.findDorms(pageable);

        // Assert
        assertEquals(expectedPage, result);
    }

    @Test
    public void testImportExcel() throws Exception {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/sample.xlsx");
        MultipartFile file = new MockMultipartFile("file", "sample.xlsx", "application/vnd.ms-excel", inputStream);

        List<Dorm> dorms = Arrays.asList(new Dorm(), new Dorm());
        when(ExcelImportUtil.importExcel(any(InputStream.class), eq(Dorm.class), any(ImportParams.class)))
                .thenReturn(Collections.singletonList(dorms));

        // Act
        Boolean result = dormService.importExcel(file);

        // Assert
        assertTrue(result);
        verify(dormRepository, times(1)).saveAll(dorms);
    }

    @Test
    public void testImportExcel_Exception() throws Exception {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/sample.xlsx");
        MultipartFile file = new MockMultipartFile("file", "sample.xlsx", "application/vnd.ms-excel", inputStream);

        when(ExcelImportUtil.importExcel(any(InputStream.class), eq(Dorm.class), any(ImportParams.class)))
                .thenThrow(new RuntimeException("Test exception"));

        // Act
        Boolean result = dormService.importExcel(file);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetDorms() {
        // Arrange
        Staffinfo staffinfo = new Staffinfo();
        staffinfo.setDormname("Main Dorm");
        staffinfo.setDormno("A1");

        List<Dorm> expectedDorms = Arrays.asList(new Dorm(), new Dorm());
        when(dormRepository.findByDormnameAndDormno(staffinfo.getDormname(), staffinfo.getDormno()))
                .thenReturn(expectedDorms);

        // Act
        List<Dorm> result = dormService.getDorms(staffinfo);

        // Assert
        assertEquals(expectedDorms, result);
    }
}