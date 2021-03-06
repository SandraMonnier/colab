/*
 * The coLAB project
 * Copyright (C) 2021 AlbaSim, MEI, HEIG-VD, HES-SO
 *
 * Licensed under the MIT License
 */
package ch.colabproject.colab.api.ejb;

import ch.colabproject.colab.api.model.WithId;
import ch.colabproject.colab.api.ws.WebsocketEndpoint;
import ch.colabproject.colab.api.ws.message.WsDeleteMessage.IndexEntry;
import java.util.HashSet;
import java.util.Set;
import javax.transaction.Status;
import javax.transaction.Synchronization;

/**
 * Simple websocket JTA synchronizer
 *
 * @author maxence
 */
public class WebsocketSynchronizer implements Synchronization {

    /**
     * set of updated entities to be propagated
     */
    private Set<WithId> updated = new HashSet<>();

    /**
     * set of entites which have been deleted during the transaction
     */
    private Set<IndexEntry> deleted = new HashSet<>();

    /**
     * TODO should assert everything is propagatable
     */
    @Override
    public void beforeCompletion() {
        // maybe nothing to do
    }

    /**
     * Called once the transaction has been fully validated. Send pending messages to their
     * respective audience
     *
     * @param status The status of the transaction completion.
     */
    @Override
    public void afterCompletion(int status) {
        if (status == Status.STATUS_COMMITTED) {
            updated.removeAll(deleted);
            if (!updated.isEmpty()) {
                WebsocketEndpoint.propagate(updated);
            }
            if (!deleted.isEmpty()) {
                WebsocketEndpoint.propagateDeletion(deleted);
            }
        }
    }

    /**
     * Register the given object within the updated set
     *
     * @param o object to register
     */
    public void registerUpdate(WithId o) {
        updated.add(o);
    }

    /**
     * register just deleted object
     *
     * @param o object which has been deleted
     */
    public void registerDelete(WithId o) {
        deleted.add(new IndexEntry(o));
    }
}
