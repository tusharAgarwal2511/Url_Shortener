package com.tushar.url_shortener.entities;

import lombok.Data;

@Data
public class UrlErrorResponseDto
{
	private String status;
	private String error;
}
