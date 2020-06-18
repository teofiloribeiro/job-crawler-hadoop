package com.teofilo.jobsCrawler.rabbitmq;

public interface FuncaoProdutor<T> {
    void funcaoASerChamada(T objeto);
}
