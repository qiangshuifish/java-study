package ink.putin.study.mq.amqp.errorhandler;

import jdk.internal.org.xml.sax.ErrorHandler;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.SAXParseException;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @date 2019-06-16 13:23
 * @apiNote
 * @since 1.0
 */
public class RabbitMqErrorHandler implements ErrorHandler {

    /**
     * 警告
     * @param e
     * @throws SAXException
     */
    @Override
    public void warning(SAXParseException e) throws SAXException {

    }

    /**
     * 异常
     * @param e
     * @throws SAXException
     */
    @Override
    public void error(SAXParseException e) throws SAXException {

    }

    /**
     * 致命，异常
     * @param e
     * @throws SAXException
     */
    @Override
    public void fatalError(SAXParseException e) throws SAXException {

    }
}
