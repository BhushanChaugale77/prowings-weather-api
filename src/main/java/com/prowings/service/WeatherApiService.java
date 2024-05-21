package com.prowings.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prowings.responce.model.WeatherData;

public interface WeatherApiService {
	
 	
	public ResponseEntity<WeatherData> getCurrentWeatherData(String city) throws JsonProcessingException;
	
}
