package com.myretail.productapi.redsky;

import java.net.URI;
import java.net.URISyntaxException;

public class RedSkyConfig {
	//public static final String PRODUCT_V2_URL = "https://redsky.target.com/v2/pdp/tcin/{tcin}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics,deep_red_labels,available_to_promise_network";

	public static final String api_host_path = "https://redsky.target.com/v2/pdp/tcin/";
	public static final String exclusion_list = "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics,deep_red_labels,available_to_promise_network";

	public static URI getProductV2URL(long tcin) throws URISyntaxException {
		String product_v2_url = api_host_path + tcin + exclusion_list;
		return new URI(product_v2_url);
	}
}
