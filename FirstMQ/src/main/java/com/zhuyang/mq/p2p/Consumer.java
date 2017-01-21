package com.zhuyang.mq.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
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
		Destination destination = null;
		MessageConsumer messageConsumer;

		try {
			cf = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
			connection = cf.createConnection();
			connection.start();
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("helloworld");
			messageConsumer = session.createConsumer(destination);
			int i=0;
			while (i<10) {
				MapMessage msg = (MapMessage) messageConsumer.receive(100000);
				if (msg != null) {
					System.out.println("message recieved:" + msg.getString("hello"+i));
					session.commit();// commit to make sure the message was consumed.  if not commit,every time we will get the message
					 Destination d = msg.getJMSReplyTo();
					 System.out.println(d);
				} else {
					break;
				}
				i++;
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
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
