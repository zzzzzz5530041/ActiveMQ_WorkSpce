package com.zhuyang.mq.pubsub;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * this is non-persist message. message can be recieve only if Subcriber is up,
 * otherwise, message cant be recieved .
 * 
 * @author Administrator
 *
 */
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
			connection.start();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			topic = session.createTopic("mytopic");
			messageConsumer = session.createConsumer(topic);
			Message msg = (Message) messageConsumer.receive();
			while (msg != null) {
				TextMessage txtMsg = (TextMessage) msg;
				System.out.println("message recieved:" + txtMsg.getText());
				session.commit();
				msg = messageConsumer.receive();
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
