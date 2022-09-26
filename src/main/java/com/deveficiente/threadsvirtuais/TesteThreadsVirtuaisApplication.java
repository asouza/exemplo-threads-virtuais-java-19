package com.deveficiente.threadsvirtuais;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class TesteThreadsVirtuaisApplication {

//	 - Precisa criar a ideia de um servidor que atender requests
//	 - O servidor executa funcoes que sao associadas a chaves
//	 - As funcoes são executadas dentro de um pool de thread
//	 - O pool de thread tem sei lá, duas threads. 
//	 - Tem funcao que faz chamada remota, tem funcao que so roda coisa em memoria ou usa io rapidinho
//	 - Se eu comecar a disparar um monte de chamada para as rotas e com pouca thread, não vai enfileirar? veremos

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication
				.run(TesteThreadsVirtuaisApplication.class, args);
		Map<String, Function<ApplicationContext, String>> rotas = new HashMap<>();
		rotas.put("/significado", contexto -> {
			DicionarioRemoto dicionario = ctx.getBean(DicionarioRemoto.class);
			return dicionario.explica("java");
		});

		rotas.put("/chuknorris", contexto -> {
			ChuckNorrisFacts chuckNorris = ctx
					.getBean(ChuckNorrisFacts.class);
			return chuckNorris.frase();
		});

		rotas.put("/memoria", contexto -> {
			return (2 * 2) + "";
		});

		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
//		ExecutorService executor = Executors.newFixedThreadPool(10);
//		ExecutorService executor = Executors.newWorkStealingPool();

		Servidor servidor = new Servidor(ctx, rotas, executor);
		List<String> listaRotas = List.of("/significado","/chuknorris","/memoria");

		for (int i = 0; i < 10; i++) {
			for (String rota : listaRotas) {
				servidor.executa(rota, resposta -> {

					System.out.println(Thread.currentThread().getName()
							+ ";Rota:" + rota + "=>" + resposta);
				});
			}
		}

		executor.shutdown();
		System.out.println("Esperando acabar tudo...");
		while (!executor.isTerminated()) {
		}

		System.out.println("Finalizado...");

	}

}
