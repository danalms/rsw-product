package com.rsw.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by lrmichaelson on 1/10/17.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PermissionDeniedException extends RuntimeException {

	private static final long serialVersionUID = -9153281592800976833L;

	public PermissionDeniedException() {
    }
}
