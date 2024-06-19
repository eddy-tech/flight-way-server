package org.intech.reservation.utils;

import org.intech.reservation.roots.CustomerEndPoint;
import org.intech.reservation.roots.FlightEndPoint;
import org.intech.reservation.roots.ReservationEndPoint;
import org.intech.reservation.roots.RootEndPoint;

public interface Utils {
    String rootEndpoint = RootEndPoint.API_ROOT;
    String customerEndpoint =  CustomerEndPoint.CUSTOMER_ENDPOINT + "/**";
    String flightEndpoint = FlightEndPoint.FLIGHT_ENDPOINT + "/**";
    String reservationEndpoint = ReservationEndPoint.CUSTOM_RESERVATION_ENDPOINT + "/**";
}
