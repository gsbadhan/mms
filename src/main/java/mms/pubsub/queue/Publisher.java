package mms.pubsub.queue;

/**
 *
 * @param <K> key of message
 * @param <M> body of message
 */
public interface Publisher<K, M> {
    void publish(K key, M message);
}
