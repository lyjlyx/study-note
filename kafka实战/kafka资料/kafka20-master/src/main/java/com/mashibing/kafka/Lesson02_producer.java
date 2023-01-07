package com.mashibing.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author: 马士兵教育
 * @create: 2021-01-26 05:13
 */
public class Lesson02_producer {

    public static String brokers = "node01:9092,node02:9092,node03:9092";
    public static Properties initConf(){
        Properties conf = new Properties();
        conf.setProperty(ProducerConfig.ACKS_CONFIG,"0");
        conf.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        conf.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        conf.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokers);
        conf.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class.getName());

        conf.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,"16384"); //16k 要调整的,分析我们msg的大小，尽量触发批次发送，减少内存碎片，和系统调用的复杂度
        conf.setProperty(ProducerConfig.LINGER_MS_CONFIG,"0");  //


        conf.setProperty(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,"1048576");
        //message.max.bytes


        conf.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG,"33554432");//32M
        conf.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG,"60000"); //60秒

        conf.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,"5");

        conf.setProperty(ProducerConfig.SEND_BUFFER_CONFIG,"32768");  //32K   -1
        conf.setProperty(ProducerConfig.RECEIVE_BUFFER_CONFIG,"32768"); //32k  -1
        return conf;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Properties conf = initConf();
        KafkaProducer<String, String> producer = new KafkaProducer<>(conf);

        while (true) {
            ProducerRecord<String, String> msg = new ProducerRecord<String, String>("ooxx", "hello", "ooxx1");

            Future<RecordMetadata> future = producer.send(msg);
            RecordMetadata recordMetadata = future.get();

        }
//        Future<RecordMetadata> send = producer.send(msg, new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata metadata, Exception exception) {
//
//            }
//        });
    }
}
