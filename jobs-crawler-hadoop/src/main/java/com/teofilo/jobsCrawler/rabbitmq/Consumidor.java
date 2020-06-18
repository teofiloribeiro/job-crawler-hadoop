package com.teofilo.jobsCrawler.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.teofilo.jobsCrawler.entities.JobDetail;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class Consumidor {

    private final static Logger LOGGER = LoggerFactory.getLogger(
            Consumidor.class.getName());

    private static final String EXCHANGE_NAME = "job";

    private final String consumerTag;

    private final String fila;

    private String ip;

    private String username;

    private String password;


    public Consumidor(String ip, String username, String password) {
        this();
        this.ip = ip;
        this.username = username;
        this.password = password;
    }

    public Consumidor() {
        this.consumerTag = EXCHANGE_NAME;
        this.fila = EXCHANGE_NAME;
    }

    @Deprecated
    public Consumidor(String fila, String consumerTag) {
        this.consumerTag = consumerTag;
        this.fila = fila;
    }

    public static String getExchangeName() {
        return EXCHANGE_NAME;
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public String getFila() {
        return fila;
    }


    @SuppressWarnings("unused")
    private Date getDate(String date) {
        try {
            DateFormat fmt = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            return fmt.parse(date);
        } catch (Exception e) {
            return new Date();
        }
    }

    /**
     * Faz parse do Json para os atributos Consumidor
     * @param json JSONObject que contem os dados necessario
     * @return Atendimento
     */
    protected JobDetail parse(JSONObject json) {
        final JobDetail jobDetail = new JobDetail();

        if (json.has("city")) {
            jobDetail.setCity(json.getString("city"));
        }

        if (json.has("role")) {
            jobDetail.setRole(json.getString("role"));
        }

        if (json.has("salary")) {
            jobDetail.setSalary(json.getFloat("salary"));
        }

        return jobDetail;
    }

    public void run(FuncaoProdutor<JobDetail> funcaoCallback) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.ip);
        factory.setUsername(this.username);
        factory.setPassword(this.password);
        factory.setAutomaticRecoveryEnabled(true);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            LOGGER.info("No construtor :: Produtor de Atendimento " +
                    "inicializado com sucesso!");

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.queueDeclare(this.fila, true, false, false, null);
            channel.queueBind(this.fila, EXCHANGE_NAME, this.consumerTag);


            LOGGER.info("run :: Thread em modo de escuta da fila {} ...",
                    this.fila);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                final String jsonString = new String(delivery.getBody(),
                        "UTF-8");

                final JSONObject json = new JSONObject(jsonString);
                LOGGER.info("run :: JobDetail como JSON recebido: {}",
                        json.toString(4));
                funcaoCallback.funcaoASerChamada(this.parse(json));
            };
            channel.basicConsume(this.fila, true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            LOGGER.error("run :: Erro na conexão do consumidor do " +
                    "JobDetail Erro: {}", e.getMessage(), e);
        }
    }
}
