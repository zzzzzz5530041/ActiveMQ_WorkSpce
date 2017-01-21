package com.zhuyang.mq.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
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
		Destination destination = null;
		MessageProducer messageProducer = null;
		try {
			// create ConnectionFactory
			cf = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
			// create activemq connection
			connection = cf.createConnection();
			connection.start();
			// create session
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// create a queue name= helloworld
			destination = session.createQueue("helloworld");
			
			// create MessageProducer
			messageProducer = session.createProducer(destination);
			for (int i = 0; i < 10; i++) {
				MapMessage msg = session.createMapMessage();
				msg.setString("hello"+i, "world"+i);
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
