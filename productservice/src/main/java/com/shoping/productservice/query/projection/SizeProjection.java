package com.shoping.productservice.query.projection;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.Size;
import com.shoping.productservice.command.data.SizeRepository;
import com.shoping.productservice.query.model.SizeResponseModel;
import com.shoping.productservice.query.queries.GetAllSizeQuery;
import com.shoping.productservice.query.queries.GetSizeDetailQuery;

@Component
public class SizeProjection {
    @Autowired
    private SizeRepository sizeRepository;

    @QueryHandler
    public List<SizeResponseModel> handle(GetAllSizeQuery query) {
        List<Size> sizes = sizeRepository.findAll();
        List<SizeResponseModel> responses = new ArrayList<>();
        sizes.forEach(size -> {
            SizeResponseModel model = new SizeResponseModel();
            BeanUtils.copyProperties(size, model);
            responses.add(model);
        });
        return responses;
    }

    @QueryHandler
    public SizeResponseModel handle(GetSizeDetailQuery query) {
        SizeResponseModel response = new SizeResponseModel();
        sizeRepository.findById(query.getId()).ifPresent(size -> {
            BeanUtils.copyProperties(size, response);
        });
        return response;
    }
}
