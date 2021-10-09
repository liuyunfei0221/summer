package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ClientConf;
import com.blue.pulsar.api.conf.ProducerConf;
import com.blue.pulsar.common.BlueTransPulsarProducer;
import org.apache.pulsar.client.api.BatcherBuilder;
import org.apache.pulsar.client.api.MessageRouter;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.blue.pulsar.utils.PulsarCommonsGenerator.generateClient;


/**
 * pulsar transaction producer coordinator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BluePulsarTransProducerCoordinator {

    private final PulsarClient pulsarClient;

    public BluePulsarTransProducerCoordinator(ClientConf conf) {
        this.pulsarClient = generateClient(conf);
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

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param <T>
     * @return
     */
    public <T extends Serializable> BlueTransPulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz) {
        return new BlueTransPulsarProducer<>(conf, pulsarClient, clz, null, null, null);
    }

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param <T>
     * @return
     */
    public <T extends Serializable> BlueTransPulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz, MessageRouter messageRouter) {
        return new BlueTransPulsarProducer<>(conf, pulsarClient, clz, messageRouter, null, null);
    }

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param messageRouter
     * @param batcherBuilder
     * @param <T>
     * @return
     */
    public <T extends Serializable> BlueTransPulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz, MessageRouter messageRouter,
                                                                                BatcherBuilder batcherBuilder) {
        return new BlueTransPulsarProducer<>(conf, pulsarClient, clz, messageRouter, batcherBuilder, null);
    }

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param messageRouter
     * @param batcherBuilder
     * @param interceptors
     * @param <T>
     * @return
     */
    public <T extends Serializable> BlueTransPulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz, MessageRouter messageRouter,
                                                                                BatcherBuilder batcherBuilder, List<ProducerInterceptor> interceptors) {
        return new BlueTransPulsarProducer<>(conf, pulsarClient, clz, messageRouter, batcherBuilder, interceptors);
    }

}
