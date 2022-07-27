package com.blue.pulsar.common;

import com.blue.pulsar.api.conf.ClientConf;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateClient;


/**
 * pulsar transaction producer coordinator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BluePulsarTransProducerCoordinator {

    private final PulsarClient pulsarClient;

    public BluePulsarTransProducerCoordinator(ClientConf conf) {
        if (conf == null)
            throw new RuntimeException("conf can't be null");

        this.pulsarClient = generateClient(conf);
    }

    public BluePulsarTransProducerCoordinator(PulsarClient pulsarClient) {
        if (pulsarClient == null)
            throw new RuntimeException("pulsarClient can't be null");

        this.pulsarClient = pulsarClient;
    }

    /**
     * start transaction
     *
     * @return
     */
    public Transaction startTransaction() {
        try {
            return pulsarClient.newTransaction().build().get();
        } catch (Exception e) {
            throw new RuntimeException("startTransaction() failed, cause: {}", e);
        }
    }

    /**
     * start transaction
     *
     * @return
     */
    public CompletableFuture<Transaction> startTransactionAsync() {
        try {
            return pulsarClient.newTransaction().build();
        } catch (Exception e) {
            throw new RuntimeException("startTransactionAsync() failed, cause: {}", e);
        }
    }

    /**
     * start transaction
     *
     * @param transTimeout
     * @param transTimeoutUnit
     * @return
     */
    public Transaction startTransaction(long transTimeout, TimeUnit transTimeoutUnit) {
        try {
            return pulsarClient.newTransaction().withTransactionTimeout(transTimeout, transTimeoutUnit).build().get();
        } catch (Exception e) {
            throw new RuntimeException("startTransaction(long transTimeout, TimeUnit transTimeoutUnit) failed, cause: {}", e);
        }
    }

    /**
     * start transaction
     *
     * @param transTimeout
     * @param transTimeoutUnit
     * @return
     */
    public CompletableFuture<Transaction> startTransactionAsync(long transTimeout, TimeUnit transTimeoutUnit) {
        try {
            return pulsarClient.newTransaction().withTransactionTimeout(transTimeout, transTimeoutUnit).build();
        } catch (Exception e) {
            throw new RuntimeException("startTransactionAsync(long transTimeout, TimeUnit transTimeoutUnit) failed, cause: {}", e);
        }
    }

    /**
     * commit transaction
     *
     * @param transaction
     */
    public void commitTransaction(Transaction transaction) {
        try {
            transaction.commit().get();
        } catch (Exception e) {
            throw new RuntimeException("commitTransaction(Transaction transaction) failed, cause: {}", e);
        }
    }

    /**
     * commit transaction
     *
     * @param transaction
     * @return
     */
    public CompletableFuture<Void> commitTransactionAsync(Transaction transaction) {
        return transaction.commit();
    }

    /**
     * rollback transaction
     *
     * @param transaction
     */
    public void abortTransaction(Transaction transaction) {
        try {
            transaction.abort().get();
        } catch (Exception e) {
            throw new RuntimeException("abortTransaction(Transaction transaction) failed, cause: {}", e);
        }
    }

    /**
     * rollback transaction
     *
     * @param transaction
     * @return
     */
    public CompletableFuture<Void> abortTransactionAsync(Transaction transaction) {
        return transaction.abort();
    }

}
