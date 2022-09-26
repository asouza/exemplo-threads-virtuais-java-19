package com.deveficiente.threadsvirtuais;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class Servidor {

	private ConfigurableApplicationContext ctx;
	private Map<String, Function<ApplicationContext, String>> rotas;
	private ExecutorService executor;

	public Servidor(ConfigurableApplicationContext ctx,
			Map<String, Function<ApplicationContext, String>> rotas,
			ExecutorService executor) {
		this.ctx = ctx;
		this.rotas = rotas;
		this.executor = executor;
	}

	public void executa(String rota,Consumer<String> canalResposta) {
		executor.execute(new Runnable() {			
			@Override
			public void run() {
				canalResposta.accept(rotas.get(rota).apply(ctx));
			}
		});

	}

}
