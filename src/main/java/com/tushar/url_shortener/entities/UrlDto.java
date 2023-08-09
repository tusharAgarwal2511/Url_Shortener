package com.tushar.url_shortener.entities;

import lombok.Data;

@Data
public class UrlDto
{
	private String url;
	private String expirationDate; // optional

}
