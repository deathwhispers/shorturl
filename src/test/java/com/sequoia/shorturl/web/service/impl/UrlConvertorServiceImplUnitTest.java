package com.sequoia.shorturl.web.service.impl;

import cn.hutool.bloomfilter.BloomFilter;
import com.sequoia.shorturl.common.server.ShortUrlGenerator;
import com.sequoia.shorturl.web.repository.UrlConvertorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UrlConvertorServiceImplUnitTest {

    private BloomFilter bloomFilter;
    private UrlConvertorRepository convertorRepository;
    private UrlConvertorServiceImpl urlConvertorService;

    @BeforeEach
    public void setUp() {
        bloomFilter = mock(BloomFilter.class);
        convertorRepository = mock(UrlConvertorRepository.class);

        urlConvertorService = new UrlConvertorServiceImpl(convertorRepository);
        ReflectionTestUtils.setField(urlConvertorService, "bloomFilter", bloomFilter);
    }

    @Test
    public void testLongUrlToShortUrl_CollisionResolution() {
        String longUrl = "http://example.com";
        String s1 = ShortUrlGenerator.generate(longUrl);
        String s2 = ShortUrlGenerator.generate(longUrl + "#");
        String s3 = ShortUrlGenerator.generate(longUrl + "##");

        // Simulate collision for s1
        when(bloomFilter.contains(s1)).thenReturn(true);
        when(convertorRepository.getLongUrlByShortUrl(s1)).thenReturn("http://other.com");

        // Simulate collision for s2
        when(bloomFilter.contains(s2)).thenReturn(true);
        when(convertorRepository.getLongUrlByShortUrl(s2)).thenReturn("http://other.com");

        // Simulate success for s3
        when(bloomFilter.contains(s3)).thenReturn(false);
        when(convertorRepository.saveIfAbsent(s3, longUrl)).thenReturn(true);

        String result = urlConvertorService.longUrlToShortUrl(longUrl);

        assertEquals(s3, result);

        // Verify interactions
        verify(bloomFilter).contains(s1);
        verify(bloomFilter).contains(s2);
        verify(bloomFilter).contains(s3);
        verify(convertorRepository).saveIfAbsent(s3, longUrl);
        verify(bloomFilter).add(s3);
    }
}
