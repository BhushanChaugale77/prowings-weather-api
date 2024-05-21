package com.prowings.service;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prowings.responce.model.WeatherData;
import com.prowings.util.CityLatLong;

@Service
public class WeatherApiServiceImpl implements WeatherApiService {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DozerBeanMapper mapper;

	double lat;
	double lon;

	@Value("${base_url}")
	String baseUrl;

	@Value("${apiKey}")
	String apiKey;

	ObjectMapper objMapper = new ObjectMapper();

	@Override
	public ResponseEntity<WeatherData> getCurrentWeatherData(String city) throws JsonProcessingException {

		// to call OpenWeatherApi
		calculateLatLong(city);

//https://api.openweathermap.org/data/2.5/weather?lat=18.6298&lon=73.7997&appid=aea2c2eaeb4020c7d96e8c22ce8d0bb2

		String dynamicUrl = baseUrl + "?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;

		System.out.println(">>>> Calling API URL: " + dynamicUrl);

		ResponseEntity<WeatherData> fetchedWeatherData = restTemplate.getForEntity(dynamicUrl, WeatherData.class);
		// map fetchedWeatherData to WeatherData
		String responseFromWeatherApi = objMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(fetchedWeatherData);
		System.out.println(">>>>>");
		System.out.println(responseFromWeatherApi);
		System.out.println("<<<<<");

		WeatherData dto = mapper.map(fetchedWeatherData.getBody(), WeatherData.class);

		String jsonStr = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
		System.out.println(">>>>>After DozerBean Mapper mapping :");
		System.out.println(jsonStr);
		System.out.println("<<<<<");
		return new ResponseEntity<WeatherData>(dto, HttpStatus.OK);

	}

	private void calculateLatLong(String city) {

		lat = CityLatLong.getLatLongCode().get(city).get(0);
		lon = CityLatLong.getLatLongCode().get(city).get(1);
	}

}
