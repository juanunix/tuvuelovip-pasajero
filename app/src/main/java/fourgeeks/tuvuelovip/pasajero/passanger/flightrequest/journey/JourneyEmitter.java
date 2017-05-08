package fourgeeks.tuvuelovip.pasajero.passanger.flightrequest.journey;

/**
 * Created by alacret on 1/29/17.
 */

public interface JourneyEmitter {
    void addJourneyListener(JourneyListener listener);
    void removeJourneyListener(JourneyListener listener);
}
