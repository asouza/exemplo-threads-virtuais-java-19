package com.deveficiente.threadsvirtuais;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://api.chucknorris.io/jokes", name = "chuck-norris")
public interface ChuckNorrisFacts {

	@GetMapping("/random")
	public String frase();
}
