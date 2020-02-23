package com.stever.gmall.payment.tests;

import com.alibaba.fastjson.JSON;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class ActivemqTest2 {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.137.60:61616");
        ((ActiveMQConnectionFactory) connectionFactory).setUserName("admin");
        ((ActiveMQConnectionFactory) connectionFactory).setPassword("123456");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            //第一个参数为false时，才有意义
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue("TEST1");

            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if(message instanceof TextMessage){
                            System.out.println(JSON.toJSONString(message));
//                            message.acknowledge();
//                            message.acknowledge();
                            //session.commit();
//                            session.commit();
                            session.recover();
                        }
                    }catch (Exception e){

                    }
                }
            });
            System.in.read();
//            session.commit();
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
