package com.example.stream.example.s3ReportCreator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@SpringBootApplication
public class S3ReportCreatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3ReportCreatorApplication.class, args);
	}

	@Bean
	Function<Message<?>, Message<?>> generate() {
		return (min) -> {

			DateFormat converter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date timestamp = new Date(Long.parseLong(Objects.requireNonNull(min.getHeaders().get("timestamp")).toString()));

			String reportName;
			reportName = converter.format(timestamp) + "_report.txt";

			return MessageBuilder
					.withPayload(Objects.requireNonNull(min.getHeaders().get("file_relativePath")))
					.setHeader("report_name", reportName)
					.setHeader(IntegrationMessageHeaderAccessor.CORRELATION_ID, converter.format(timestamp))
					.build();
		};
	}
}
