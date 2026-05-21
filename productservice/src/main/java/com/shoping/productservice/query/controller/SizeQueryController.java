package com.shoping.productservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.productservice.query.model.SizeResponseModel;
import com.shoping.productservice.query.queries.GetAllSizeQuery;
import com.shoping.productservice.query.queries.GetSizeDetailQuery;

@RestController
@RequestMapping("/api/v1/size")
public class SizeQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    @ApiMessage("Get sizes")
    public ResponseEntity<List<SizeResponseModel>> getAllSizes() {
        GetAllSizeQuery query = new GetAllSizeQuery();
        List<SizeResponseModel> result = queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(SizeResponseModel.class)).join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{sizeId}")
    @ApiMessage("Get size detail")
    public ResponseEntity<SizeResponseModel> getSizeDetail(@PathVariable String sizeId) {
        GetSizeDetailQuery query = new GetSizeDetailQuery(sizeId);
        SizeResponseModel result = queryGateway
                .query(query, ResponseTypes.instanceOf(SizeResponseModel.class)).join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
