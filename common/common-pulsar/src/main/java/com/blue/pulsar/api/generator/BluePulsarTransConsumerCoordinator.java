package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.common.BluePulsarConsumer;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * pulsar消费者构建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "ControlFlowStatementWithoutBraces", "AlibabaAvoidComplexCondition"})
public final class BluePulsarTransConsumerCoordinator {

    private final PulsarClient pulsarClient;

    public BluePulsarTransConsumerCoordinator(PulsarClient pulsarClient) {
        this.pulsarClient = pulsarClient;
    }

    /**
     * 开启事务
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
     * 开启事务
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
     * 开启事务
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
     * 开启事务
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
     * 提交事务
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
     * 提交事务
     *
     * @param transaction
     * @return
     */
    public CompletableFuture<Void> commitTransactionAsync(Transaction transaction) {
        return transaction.commit();
    }

    /**
     * 撤销事务
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
     * 撤销事务
     *
     * @param transaction
     * @return
     */
    public CompletableFuture<Void> abortTransactionAsync(Transaction transaction) {
        return transaction.abort();
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer) {
        return new BluePulsarConsumer<>(conf, consumer, null, null, null, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, null, null, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, consumerEventListener, null, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener,
                                                                           ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, consumerEventListener, interceptors, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener,
                                                                           List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, consumerEventListener, interceptors, keySharedPolicy);
    }

}
