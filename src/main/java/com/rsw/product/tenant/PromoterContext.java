package com.rsw.product.tenant;


import com.rsw.product.domain.Promoter;

/**
 * Created by DAlms on 2/19/17.
 * Thread-based holder for current promoter
 */
public class PromoterContext {
    private static ThreadLocal<Promoter> currentPromoter = new ThreadLocal<Promoter>();

    public static void setCurrentPromoter(Promoter promoter) {
        currentPromoter.set(promoter);
    }

    public static Promoter getCurrentPromoter() {
        return currentPromoter.get();
    }

    public static void clear() {
        currentPromoter.set(null);
    }
}
