package com.tushar.url_shortener.service;

import com.google.common.hash.Hashing;
import com.tushar.url_shortener.entities.Url;
import com.tushar.url_shortener.entities.UrlDto;
import com.tushar.url_shortener.repositories.UrlRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService
{
	@Autowired
	private UrlRepository urlRepository;

	@Override
	public Url generateShortLink(UrlDto urlDto)
	{
		if (StringUtils.isNotEmpty(urlDto.getUrl()))
		{
			String encodedUrl   = createShortUrl(urlDto.getUrl());
			Url    urlToPersist = new Url();
			urlToPersist.setCreationDate(LocalDateTime.now());
			urlToPersist.setOriginalUrl(urlDto.getUrl());
			urlToPersist.setShortUrl(encodedUrl);
			urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
			Url urlToReturn = persistShortLink(urlToPersist);
			return urlToReturn;
		}
		return null;
	}

	private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate)
	{
		if (StringUtils.isBlank(expirationDate))
		{
			return creationDate.plusSeconds(300);
		}
		LocalDateTime expirationDateToReturn = LocalDateTime.parse(expirationDate);
		return expirationDateToReturn;
	}

	private String createShortUrl(String url)
	{
		String        encodedUrl = "";
		LocalDateTime time       = LocalDateTime.now();
		encodedUrl = Hashing.murmur3_32().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
		return encodedUrl;
	}

	@Override
	public Url persistShortLink(Url url)
	{
		Url urlToReturn = urlRepository.save(url);
		return urlToReturn;
	}

	@Override
	public Url getUrlEncoded(String url)
	{
		Url urlToReturn = urlRepository.findByShortUrl(url);
		return urlToReturn;
	}

	@Override
	public void deleteShortLink(Url url)
	{
		urlRepository.delete(url);
	}
}
