package aiad.feup.intentions;

import aiad.feup.messageObjects.Offer;

/**
 * Accept an offer intention
 */
public class AcceptOffer extends Intention {
    private static AcceptOffer instance;

    /**
     * Offer for being accepted or not
     */
    private Offer offer;

    public static AcceptOffer getInstance() {
        if(instance == null)
            instance = new AcceptOffer();
        return instance;
    }

    private AcceptOffer() {
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Override
    public void calculateWeight() {

    }
}
