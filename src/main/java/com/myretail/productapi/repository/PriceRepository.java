package com.myretail.productapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myretail.productapi.model.Price;

public interface PriceRepository extends MongoRepository<Price, Long> {

}
