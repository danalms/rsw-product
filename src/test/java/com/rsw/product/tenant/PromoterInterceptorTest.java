package com.rsw.product.tenant;

import com.rsw.product.domain.Promoter;
import com.rsw.product.utils.PromoterModelUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by DAlms on 2/26/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PromoterInterceptorTest {
    @Mock
    private PromoterCache promoterCache;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private PromoterInterceptor subject;

    private Map<String,String> paramMap = new HashMap<>();

    @Before
    public void setup() {
        PromoterContext.clear();
        paramMap.clear();
        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(paramMap);
    }

    @Test
    public void preHandle_happyPath() throws Exception {
        paramMap.put("promoterId", "42");

        Promoter promoter = PromoterModelUtils.buildPromoter(42L);
        when(promoterCache.findPromoter(42L)).thenReturn(Optional.of(promoter));

        boolean result = subject.preHandle(request, response, "foo");
        assertTrue(result);
        verify(promoterCache, times(1)).findPromoter(42L);
        verify(response, never()).sendError(anyInt(),anyString());
    }

    @Test
    public void preHandle_noPromoterId() throws Exception {
        paramMap.put("promoterId", "");

        boolean result = subject.preHandle(request, response, "foo");
        assertFalse(result);
        verify(promoterCache, never()).findPromoter(anyLong());
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCaptor.capture(), messageCaptor.capture());
        assertThat(statusCaptor.getValue()).isEqualTo(403);
    }

    @Test
    public void preHandle_nonExistPromoterId() throws Exception {
        paramMap.put("promoterId", "42");

        when(promoterCache.findPromoter(42L)).thenReturn(Optional.empty());

        boolean result = subject.preHandle(request, response, "foo");
        assertFalse(result);
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCaptor.capture(), messageCaptor.capture());
        assertThat(statusCaptor.getValue()).isEqualTo(403);
    }

    @Test
    public void preHandle_nullOptional() throws Exception {
        paramMap.put("promoterId", "42");

        when(promoterCache.findPromoter(42L)).thenReturn(null);

        boolean result = subject.preHandle(request, response, "foo");
        assertFalse(result);
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCaptor.capture(), messageCaptor.capture());
        assertThat(statusCaptor.getValue()).isEqualTo(403);
    }

    @Test
    public void preHandle_invalidPromoterId() throws Exception {
        paramMap.put("promoter_id", "abc");

        boolean result = subject.preHandle(request, response, "foo");
        assertFalse(result);
        verify(promoterCache, never()).findPromoter(anyLong());
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCaptor.capture(), messageCaptor.capture());
        assertThat(statusCaptor.getValue()).isEqualTo(403);
    }
}