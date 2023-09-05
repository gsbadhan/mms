package mms.service.cart;

import mms.dto.cart.CartUpsertRequest;
import mms.dto.cart.CartUpsertResponse;

public interface CartService {
    CartUpsertResponse saveCart(CartUpsertRequest request) throws Exception;

    CartUpsertResponse updateCart(String id, CartUpsertRequest request) throws Exception;

    CartUpsertResponse getCart(String id) throws Exception;

    void deleteCart(String id) throws Exception;

    CartUpsertResponse confirmCart(String id, CartUpsertRequest request) throws Exception;
}
