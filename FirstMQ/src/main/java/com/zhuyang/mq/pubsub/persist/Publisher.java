package com.zhuyang.mq.pubsub.persist;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {
	// default connection username
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	// default connection password
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	// default connection url
	private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void main(String[] args) {
		ConnectionFactory cf = null;
		Connection connection = null;
		// session used to revieve or send
		Session session = null;
		// message destination
		Topic topic = null;
		MessageProducer messageProducer = null;
		// create ConnectionFactory
		try {
			cf = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
			// create activemq connection
			connection = cf.createConnection();
			
			// create session
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// create a topic name= mytopic
			topic = session.createTopic("mytopic_persistent");
			// create MessageProducer
			messageProducer = session.createProducer(topic);
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT); //set persistent
			connection.start();
			for (int i = 0; i < 10; i++) {
				TextMessage msg = session.createTextMessage("mytopic_persistent" + i);
				messageProducer.send(msg);
			}
			session.commit();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
