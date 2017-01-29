package com.rsw.product.domain;

/**
 * Created by DAlms on 12/18/16.
 */
public class EntityIdResponse {
    private Long id;

    public EntityIdResponse() {
    }

    public EntityIdResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
