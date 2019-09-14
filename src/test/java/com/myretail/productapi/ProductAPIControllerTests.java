package com.myretail.productapi;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.myretail.productapi.model.Price;
import com.myretail.productapi.repository.PriceRepository;
import com.myretail.productapi.vo.PriceVO;
import com.myretail.productapi.vo.ProductVO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class ProductAPIControllerTests {
	
	//@LocalServerPort
	int definedPort = 8080;
    
    URI uri;
    
	private RestTemplate restTemplate;
	
	final String baseUrl = "http://localhost:" + definedPort + "/api/products";
    
	private static final long SUCCESS_PRODUCT_ID = 13860420;
	private ProductVO successProductVO;
	private PriceVO successtPriceVO;
	private Price successPrice;

	private static final long PRODUCT_NO_PRICE_ID = 13860428;
	private ProductVO noPriceProductVO;

	private static final long PRODUCT_NOT_EXIST_ID = -99999999;
	
	private static final double NEW_PRICE_VALUE = 89.99;
	private static final double OLD_PRICE_VALUE = 10.99;
	
	private static final long PRODUCT_PRICE_UPDATE_TEST_ID = 13860429;
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Before
	public void setUp() throws Exception {
		restTemplate = new RestTemplate();

		successProductVO = new ProductVO();
		successtPriceVO = new PriceVO();
		successPrice = new Price();

		successPrice.setId(SUCCESS_PRODUCT_ID);
		successPrice.setCurrencyCode("USD");
		successPrice.setValue(OLD_PRICE_VALUE);

		successProductVO.setId(SUCCESS_PRODUCT_ID);
		successProductVO.setName("Final Destination 5 (dvd_video)");
		successtPriceVO.setCurrencyCode(successPrice.getCurrencyCode());
		successtPriceVO.setValue(successPrice.getValue());
		successProductVO.setPriceVO(successtPriceVO);
		
		noPriceProductVO = new ProductVO();
		noPriceProductVO.setId(PRODUCT_NO_PRICE_ID);
		noPriceProductVO.setName("The Big Lebowski (Blu-ray)");
		noPriceProductVO.setPriceVO(null);
		
		//This is to setup DB for testing purpose
		priceRepository.save(successPrice);
		priceRepository.delete(new Price(PRODUCT_NO_PRICE_ID,"USD",0.0));
	}
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testFindBySuccess() throws URISyntaxException{
		String testURL = baseUrl + "/"+SUCCESS_PRODUCT_ID;
		uri = new URI(testURL);
		ResponseEntity<ProductVO> result = restTemplate.getForEntity(uri, ProductVO.class);
		
		//Verify request succeed
	    assertEquals(200, result.getStatusCodeValue());
	    assertEquals(successProductVO, result.getBody());
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testFindByWithoutId() throws URISyntaxException{
		uri = new URI(baseUrl);
		@SuppressWarnings("unused")
		ResponseEntity<ProductVO> result = restTemplate.getForEntity(uri, ProductVO.class);
	}
	
	@Test
	public void testFindByProductNoPrice() throws URISyntaxException{
		String testURL = baseUrl + "/"+PRODUCT_NO_PRICE_ID;
		uri = new URI(testURL);
		ResponseEntity<ProductVO> result = restTemplate.getForEntity(uri, ProductVO.class);
		
		//Verify request succeed
	    assertEquals(200, result.getStatusCodeValue());
	    assertEquals(noPriceProductVO, result.getBody());
	}
	
	@Test(expected=HttpServerErrorException.class)
	public void testFindByNoProduct() throws URISyntaxException{
		String testURL = baseUrl + "/"+PRODUCT_NOT_EXIST_ID;
		uri = new URI(testURL);
		@SuppressWarnings("unused")
		ResponseEntity<ProductVO> result = restTemplate.getForEntity(uri, ProductVO.class);
	}
	
	@Test
	public void testupdatePriceSuccess() throws URISyntaxException{
		String testURL = baseUrl + "/"+SUCCESS_PRODUCT_ID;
		uri = new URI(testURL);
		successProductVO.getPriceVO().setValue(NEW_PRICE_VALUE);
		ProductVO productVO = successProductVO;
		restTemplate.put(uri, productVO);
		ResponseEntity<ProductVO> result = restTemplate.getForEntity(uri, ProductVO.class);
		//Verify request succeed
	    assertEquals(200, result.getStatusCodeValue());
	    assertEquals(successProductVO, result.getBody());
	    
		successProductVO.getPriceVO().setValue(OLD_PRICE_VALUE);
		productVO = successProductVO;
		restTemplate.put(uri, productVO);
		result = restTemplate.getForEntity(uri, ProductVO.class);
		//Verify request succeed
	    assertEquals(200, result.getStatusCodeValue());
	    assertEquals(successProductVO, result.getBody());
	    
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testupdatePriceWithoutBody() throws URISyntaxException{
		String testURL = baseUrl + "/"+PRODUCT_PRICE_UPDATE_TEST_ID;
		uri = new URI(testURL);

		restTemplate.put(uri, null);
	    
	}
	
	@Test(expected=HttpClientErrorException.BadRequest.class)
	public void testupdatePriceWithoutPrice() throws URISyntaxException{
		String testURL = baseUrl + "/"+PRODUCT_PRICE_UPDATE_TEST_ID;
		uri = new URI(testURL);
		ProductVO productVO = successProductVO;
		productVO.setPriceVO(null);
		restTemplate.put(uri, productVO); 
	}
	
	@Test(expected=HttpClientErrorException.BadRequest.class)
	public void testupdatePriceWrongProduct() throws URISyntaxException{
		String testURL = baseUrl + "/"+PRODUCT_PRICE_UPDATE_TEST_ID;
		uri = new URI(testURL);
		ProductVO productVO = successProductVO;
		restTemplate.put(uri, productVO); 
	}
}
