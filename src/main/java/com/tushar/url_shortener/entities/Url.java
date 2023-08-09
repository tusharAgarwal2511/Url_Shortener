package com.tushar.url_shortener.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Url
	{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long          id;
	@Lob
	private String        originalUrl;
	private String        shortUrl;
	private LocalDateTime creationDate;
	private LocalDateTime expirationDate;

}
