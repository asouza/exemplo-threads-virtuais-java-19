package com.deveficiente.threadsvirtuais;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://api.dictionaryapi.dev/api/v2/entries/en", name = "dicionario")
public interface DicionarioRemoto {

	
	@GetMapping("/{palavra}")
	public String explica(@PathVariable("palavra") String palavra);
}
