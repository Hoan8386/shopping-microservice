package com.shoping.productservice.query.controller;

import java.math.BigDecimal;
import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.productservice.query.model.ProductResponseModel;
import com.shoping.productservice.query.queries.FilterProductQuery;
import com.shoping.productservice.query.queries.GetAllProductQuery;
import com.shoping.productservice.query.queries.GetProductDetailQuery;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/products")
public class BookQueryController {
        @Autowired
        private QueryGateway queryGateway;

        // 1. BookQueryController: lớp REST, chỉ làm nhiệm vụ nhận HTTP và bắn query.
        // 2. GetAllProductQuery: query marker, không cần field vì chỉ yêu cầu lấy tất
        // cả.
        // 3. GetProductDetailQuery: query có Id để lấy 1 sản phẩm.
        // 4. BookProjection: nơi xử lý query bằng @QueryHandler.
        // 5. ProductResponseModel: DTO để trả dữ liệu ra ngoài API.
        @GetMapping
        @ApiMessage("Get products")
        public ResponseEntity<List<ProductResponseModel>> GetAllProducts() {
                GetAllProductQuery getAllProductQuery = new GetAllProductQuery();
                List<ProductResponseModel> result = queryGateway
                                .query(getAllProductQuery,
                                                ResponseTypes.multipleInstancesOf(ProductResponseModel.class))
                                .join();
                return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        @GetMapping("{ProductId}")
        @ApiMessage("Get product detail")
        public ResponseEntity<List<ProductResponseModel>> GetProductDetailQuery(@PathVariable String ProductId) {
                GetProductDetailQuery getAllProductQuery = new GetProductDetailQuery(ProductId);
                List<ProductResponseModel> result = queryGateway
                                .query(getAllProductQuery,
                                                ResponseTypes.multipleInstancesOf(ProductResponseModel.class))
                                .join();
                return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // Filter sản phẩm
        @GetMapping("/filter")
        @ApiMessage("Filter products")
        public ResponseEntity<List<ProductResponseModel>> filterProducts(
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String category,
                        @RequestParam(required = false) Double minPrice,
                        @RequestParam(required = false) Double maxPrice) {
                FilterProductQuery query = new FilterProductQuery(
                                name,
                                category,
                                minPrice,
                                maxPrice);
                List<ProductResponseModel> result = queryGateway.query(
                                query,
                                ResponseTypes.multipleInstancesOf(ProductResponseModel.class)).join();
                return ResponseEntity.status(HttpStatus.OK).body(result);
        }

}
