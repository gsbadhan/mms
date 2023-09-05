package mms.service.cart;

import mms.dao.cart.CartBookingCacheRepository;
import mms.dao.cart.SeatLockRepository;
import mms.dto.cart.CartDetail;
import mms.dto.cart.CartUpsertRequest;
import mms.dto.cart.CartUpsertResponse;
import mms.exception.SeatNotAvailableException;
import mms.pubsub.message.Operation;
import mms.pubsub.message.SeatLockMessage;
import mms.pubsub.queue.ShowsSeatLockPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartOperations cartOperations;
    @Autowired
    private CartBookingCacheRepository cartBookingCache;
    @Autowired
    private SeatLockRepository seatLockRepository;
    @Autowired
    private ShowsSeatLockPublisher seatLockPublisher;

    @Override
    public CartUpsertResponse saveCart(CartUpsertRequest request) throws Exception {
        CartDetail cartDetail = CartDetail.copy(request);
        cartOperations.verifyCart(cartDetail);
        cartOperations.computeBill(cartDetail);
        cartBookingCache.saveBookingDetail(cartDetail.getCartId(), cartDetail);
        CartUpsertResponse response = new CartUpsertResponse(cartDetail.getCartId(), request.getSessionId(),
                request.getCityId(), request.getTheatreId(), request.getAudiId(), request.getShowId(), request.getSeats(),
                cartDetail.getCartBillDetail());
        return response;
    }

    @Override
    public CartUpsertResponse updateCart(String id, CartUpsertRequest request) throws Exception {
        CartDetail cartDetail = CartDetail.copy(id, request);
        cartBookingCache.isBookingDetailExists(id);
        cartOperations.verifyCart(cartDetail);
        cartOperations.computeBill(cartDetail);
        cartBookingCache.saveBookingDetail(cartDetail.getCartId(), cartDetail);
        CartUpsertResponse response = new CartUpsertResponse(cartDetail.getCartId(), request.getSessionId(),
                request.getCityId(), request.getTheatreId(), request.getAudiId(), request.getShowId(), request.getSeats(),
                cartDetail.getCartBillDetail());
        return response;
    }

    @Override
    public CartUpsertResponse confirmCart(String id, CartUpsertRequest request) throws Exception {
        CartDetail cartDetail = CartDetail.copy(id, request);
        cartBookingCache.isBookingDetailExists(id);
        cartOperations.verifyCart(cartDetail);
        cartOperations.computeBill(cartDetail);
        try {
            seatLockRepository.blockSeat(cartDetail.getShowId(), cartDetail.getSeats());
        } catch (SeatNotAvailableException snae) {
            seatLockRepository.unblockSeat(cartDetail.getShowId(), cartDetail.getSeats());
            throw snae;
        }
        cartBookingCache.saveBookingDetail(cartDetail.getCartId(), cartDetail);
        seatLockPublisher.publish(cartDetail.getShowId().toString(), new SeatLockMessage(cartDetail.getShowId(),
                cartDetail.getSeats(), null, Operation.create));
        CartUpsertResponse response = new CartUpsertResponse(cartDetail.getCartId(), request.getSessionId(),
                request.getCityId(), request.getTheatreId(), request.getAudiId(), request.getShowId(), request.getSeats(),
                cartDetail.getCartBillDetail());
        return response;
    }

    @Override
    public CartUpsertResponse getCart(String id) throws Exception {
        cartBookingCache.isBookingDetailExists(id);
        CartDetail cartDetail = cartBookingCache.getBookingDetail(id);
        return CartUpsertResponse.copy(cartDetail);
    }

    @Override
    public void deleteCart(String id) throws Exception {
        cartBookingCache.isBookingDetailExists(id);
        cartBookingCache.deleteBookingDetail(id);
    }

}
