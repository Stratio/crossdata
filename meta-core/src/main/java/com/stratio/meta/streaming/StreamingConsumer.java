package com.stratio.meta.streaming;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//https://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+Example

public class StreamingConsumer {

    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public StreamingConsumer(String topic, String zookeeper, String groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                new ConsumerConfig(props));
        this.topic = topic;
    }
    public void shutdown() {
        if (consumer != null){
            consumer.shutdown();
        }
        if (executor != null){
            executor.shutdown();
        }
    }

    public void run(int numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // Now launch all the threads
        executor = Executors.newFixedThreadPool(numThreads);

        // Now create an object to consume the messages
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new KafkaConsumer(stream, threadNumber));
            threadNumber++;
        }
    }

    public class KafkaConsumer implements Runnable {
        private KafkaStream stream;
        private int threadNumber;

        public KafkaConsumer(KafkaStream stream, int threadNumber) {
            this.threadNumber = threadNumber;
            this.stream = stream;
        }

        public void run() {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()){
                System.out.println("Thread " + threadNumber + ": " + new String(it.next().message()));
            }
            System.out.println("Shutting down Thread: " + threadNumber);
        }
    }
}
