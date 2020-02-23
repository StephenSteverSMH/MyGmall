package com.stever.gmall.payment.tests;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class ActivemqTest {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.137.60:61616");
        ((ActiveMQConnectionFactory) connectionFactory).setUserName("admin");
        ((ActiveMQConnectionFactory) connectionFactory).setPassword("123456");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);//开启事务
            Queue queue = session.createQueue("TEST1");
            //queue模式
            MessageProducer messageProducer = session.createProducer(queue);
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText("来杯汽水2");
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer.send(textMessage);

            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            //....
        }finally {
            if(connection!=null){
                connection.close();
            }
        }
    }
}
