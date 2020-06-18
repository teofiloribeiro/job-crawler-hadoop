package com.teofilo.jobsCrawler.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.teofilo.jobsCrawler.entities.JobDetail;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Produtor {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            Produtor.class);

    private static final String EXCHANGE_NAME = "jobs";

    private final ConnectionFactory factory;

    @Deprecated
    public Produtor() {
        this.factory = new ConnectionFactory();
        this.factory.setHost("138.197.71.50");
        this.factory.setUsername("paulo");
        this.factory.setPassword("paulo");
    }

    public Produtor(String ip, String username, String password) {
        this.factory = new ConnectionFactory();
        this.factory.setHost(ip);
        this.factory.setUsername(username);
        this.factory.setPassword(password);
    }

    /**
     * Publica o JobDetail na filha "agendamento"
     * @param jobDetail Jobdetail a ser publicado
     */
    public void publish(JobDetail jobDetail) {
        LOGGER.info("publish :: Publicando informações de trabalho em " +
                "jobdetail na fila de {} ", jobDetail);
        this.publish(jobDetail, EXCHANGE_NAME, "JobDetail");
    }


    /**
     * Publica qualquer objeto na fila especificada
     * @param object Objeto a ser publicado
     * @param queue nome da fila
     * @param objectName Nome do objeto
     * @param <T> Tipo do objeto
     */
    private <T> void publish(T object, String queue, String objectName) {
        final JSONObject json = new JSONObject(object);

        try (Connection connection = this.factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");


            channel.queueDeclare(queue, true, false, false, null);
            channel.basicPublish(EXCHANGE_NAME, queue, null,
                    json.toString().getBytes());

            // se o nome do objeto nao for informado
            if (objectName == null) {
                StringBuilder sb = new StringBuilder(queue);
                sb.setCharAt(0, Character.toUpperCase(queue.charAt(0)));
                objectName = sb.toString();
            }

            LOGGER.info("publish :: {} publicado na fila de {} com sucesso." +
                    " Objeto: {}", objectName, queue,
                    json.toString(4));
        } catch (Exception e) {
            LOGGER.error("publish :: Erro ao publicar {} Objeto: {} Erro: " +
                "{}", objectName, json.toString(4),
                    e.getMessage(), e);
        }
    }


}
