package aiad.feup.messageObjects;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Round Information content object
 * stores the information of all the final, accepted offers of a manager
 */
public class RoundInformation implements Serializable {

    /**
     * The serial version UUID
     */
    private static final long serialVersionUID = 3115174607536408483L;

    /**
     * The offers accepted by the manager.
     */
    private List<Offer> managerOffers;

    public RoundInformation(List<Offer> offers) {
        this.managerOffers = offers;
    }

    public List<Offer> getManagerOffers(){
        return this.managerOffers;
    }


}
