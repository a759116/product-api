package com.myretail.productapi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.productapi.model.Price;
import com.myretail.productapi.redsky.RedSkyConfig;
import com.myretail.productapi.repository.PriceRepository;
import com.myretail.productapi.vo.PriceVO;
import com.myretail.productapi.vo.ProductVO;

@RestController
@RequestMapping("/api/products")
public class ProductAPIController {
	@Autowired
	PriceRepository priceRepository;

	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper objectMapper = new ObjectMapper();

	/*
	 * Responds to an HTTP GET request at /products/{id} and delivers product
	 * data as JSON (where {id} will be a number.
	 */

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ProductVO findBy(@PathVariable("id") long id) {
		
		ProductVO productVO = new ProductVO();

		ResponseEntity<String> redSkyResponse;
		
		try {
			redSkyResponse = restTemplate.getForEntity(RedSkyConfig.getProductV2URL(id), String.class);
		} catch (RestClientException e1) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "We are looking into this");
			//log error
		} catch (URISyntaxException e1) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "We are looking into this");
			//log error
		}
		
		if(redSkyResponse.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(redSkyResponse.getStatusCode(), "Something went wrong with RedSky API ");
		}
		
		String productResponse = redSkyResponse.getBody();

		JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(productResponse);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "We are looking into this");
			//log error
		}

		String name = jsonNode.at("/product/item/product_description/title").asText();
		
		//assume that name can be blank or not available
		productVO.setId(id);
		productVO.setName(name);

		Optional<Price> result = priceRepository.findById(id);

		if (result.isPresent()) {
			PriceVO priceVO = new PriceVO();
			priceVO.setCurrencyCode(result.get().getCurrencyCode());
			priceVO.setValue(result.get().getValue());
			productVO.setPriceVO(priceVO);
		} 
		else {
			//assume that price not found is a valid scenario
			productVO.setPriceVO(null);
		}

		return productVO;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ProductVO updatePrice(@PathVariable("id") long id, @RequestBody ProductVO productVO) {

		if (productVO.getPriceVO() == null || productVO.getId() != id) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check your data");
		}

		//assume Price model will validate data and throw exceptions, and then handle those exceptions
		
		Price price = new Price(id, productVO.getPriceVO().getCurrencyCode(),
				productVO.getPriceVO().getValue());

		Price updatedPrice = priceRepository.save(price);
		
		ProductVO updProductVO = new ProductVO();
		updProductVO.setId(id);
		updProductVO.setName(productVO.getName());
		
		PriceVO priceVO = new PriceVO();
		priceVO.setCurrencyCode(updatedPrice.getCurrencyCode());
		priceVO.setValue(updatedPrice.getValue());
		updProductVO.setPriceVO(priceVO);
		
		return updProductVO;
	}

}
