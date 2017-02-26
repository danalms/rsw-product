package com.rsw.product.tenant;

import com.rsw.product.domain.Promoter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * Created by DAlms on 2/19/17.
 * Intercept the "promoterId" path param, using the Spring request mapping template variables provided in
 * the request for this type of interceptor.
 * Lookup the Promoter and set the object into the PromoterContext for downstream schema resolution
 * (Hibernate's CurrentTenantIdentifierResolver)
 */
public class PromoterInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(PromoterInterceptor.class);

    private static final String TEMPLATE_PROMOTER_ID = "promoterId";

    private PromoterCache promoterCache;

    public PromoterInterceptor(PromoterCache promoterCache) {
        this.promoterCache = promoterCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Map<String, String> parameterMap = (Map<String,String>)
                request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        // Clear the current promoter context
        PromoterContext.clear();

        if (parameterMap.containsKey(TEMPLATE_PROMOTER_ID)) {
            String value = parameterMap.get(TEMPLATE_PROMOTER_ID);
            if (!StringUtils.isEmpty(value)) {
                try {
                    // Get the Promoter from the application level cache.
                    Optional<Promoter> promoter = promoterCache.findPromoter(Long.parseLong(value));
                    if (promoter != null && promoter.isPresent()) {
                        PromoterContext.setCurrentPromoter(promoter.get());
                    }
                } catch (NumberFormatException e) {
                    LOG.error("Path promoterId parameter is not a number", value);
                }
            }
        }

        if (PromoterContext.getCurrentPromoter() == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing promoterId in request path");
            return false;
        }

        return true;
    }
}