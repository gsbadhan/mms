package mms.dao.cart;

import mms.exception.SeatNotAvailableException;

import java.util.Set;

public interface SeatLockRepository {
    void blockSeat(Integer showId, Set<Integer> seats) throws SeatNotAvailableException;
    void unblockSeat(Integer showId, Set<Integer> seats);
}
