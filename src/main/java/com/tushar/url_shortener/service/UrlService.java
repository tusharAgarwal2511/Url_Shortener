package com.tushar.url_shortener.service;


import com.tushar.url_shortener.entities.Url;
import com.tushar.url_shortener.entities.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService
{
	Url generateShortLink(UrlDto urlDto);

	Url persistShortLink(Url url);

	Url getUrlEncoded(String url);

	void deleteShortLink(Url url);

}
