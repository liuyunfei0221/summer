package com.blue.pulsar.component;

import com.blue.pulsar.api.conf.ClientConf;
import com.blue.pulsar.api.conf.ConsumerConf;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.blue.pulsar.common.PulsarCommonsGenerator.generateClient;

/**
 * pulsar transaction consumer generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "ControlFlowStatementWithoutBraces", "AlibabaAvoidComplexCondition", "AliControlFlowStatementWithoutBraces"})
public final class BluePulsarTransConsumerCoordinator {

    private final PulsarClient pulsarClient;

    public BluePulsarTransConsumerCoordinator(ClientConf conf) {
        if (conf == null)
            throw new RuntimeException("conf can't be null");

        this.pulsarClient = generateClient(conf);
    }

    public BluePulsarTransConsumerCoordinator(PulsarClient pulsarClient) {
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
            throw new RuntimeException("startTransaction failed, cause: {}", e);
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
            throw new RuntimeException("startTransactionAsync failed, cause: {}", e);
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
            throw new RuntimeException("startTransaction failed, cause: {}", e);
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
            throw new RuntimeException("startTransactionAsync failed, cause: {}", e);
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
            throw new RuntimeException("commitTransaction failed, cause: {}", e);
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
            throw new RuntimeException("abortTransaction failed, cause: {}", e);
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

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, null, null, null);
    }

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer, ConsumerEventListener consumerEventListener) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, consumerEventListener, null, null);
    }

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer, ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, consumerEventListener, interceptors, null);
    }

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer, ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, consumerEventListener, interceptors, keySharedPolicy);
    }

}
