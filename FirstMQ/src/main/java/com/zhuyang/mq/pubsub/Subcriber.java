package com.zhuyang.mq.pubsub;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Subcriber {
	// default connection username
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	// default connection password
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	// default connection url
	private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void main(String[] args) {
		ConnectionFactory cf = null;
		Connection connection = null;
		Session session = null;
		Topic topic = null;
		MessageConsumer messageConsumer;

		try {
			cf = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
			connection = cf.createConnection();
			//set a client id for the connection
			connection.setClientID("clientID002");
			connection.start();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			topic = session.createTopic("mytopic");
			messageConsumer = session.createDurableSubscriber(topic, "mysub");
			while (true) {
				TextMessage msg = (TextMessage) messageConsumer.receive();
				if (msg != null) {
					System.out.println("message recieved:" + msg.getText());
				} else {
					break;
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
