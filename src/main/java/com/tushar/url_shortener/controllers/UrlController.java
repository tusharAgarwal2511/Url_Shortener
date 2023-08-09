package com.tushar.url_shortener.controllers;

import com.tushar.url_shortener.entities.Url;
import com.tushar.url_shortener.entities.UrlDto;
import com.tushar.url_shortener.entities.UrlErrorResponseDto;
import com.tushar.url_shortener.entities.UrlResponseDto;
import com.tushar.url_shortener.service.UrlServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/url/")
@Tag(name = "Url")
public class UrlController
{
	@Autowired
	private UrlServiceImpl urlService;

	@CrossOrigin
	@PostMapping("/generate/")
	@Operation(description = "Post endpoint for Url", summary = "Generate a new short url")
	public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto)
	{
		Url url = urlService.generateShortLink(urlDto);
		if (url != null)
		{
			UrlResponseDto urlResponseDto = new UrlResponseDto();
			urlResponseDto.setOriginalUrl(url.getOriginalUrl());
			urlResponseDto.setShortLink(url.getShortUrl());
			urlResponseDto.setExpirationDate(url.getExpirationDate());
			return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.CREATED);
		}
		UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
		urlErrorResponseDto.setStatus("404");
		urlErrorResponseDto.setError("There was an error processing your error, please try again.");
		return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping("/redirect/{shortUrl}")
	@Operation(description = "Get endpoint for Url", summary = "Redirect to the link mentioned in short url")
	public ResponseEntity<?> redirectToOriginalUrl(@PathVariable("shortUrl") String shortUrl, HttpServletResponse response) throws IOException
	{
		if (StringUtils.isEmpty(shortUrl))
		{
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setStatus("404");
			urlErrorResponseDto.setError("Invalid Url");
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
		}
		Url urlToReturn = urlService.getUrlEncoded(shortUrl);
		if (urlToReturn == null)
		{
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setStatus("404");
			urlErrorResponseDto.setError("Url doesn't exist");
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
		}

		if (urlToReturn.getExpirationDate().isBefore(LocalDateTime.now()))
		{
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setStatus("400");
			urlErrorResponseDto.setError("Url Expired");
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
		}

		response.sendRedirect(urlToReturn.getOriginalUrl());
		return null;
	}

}
