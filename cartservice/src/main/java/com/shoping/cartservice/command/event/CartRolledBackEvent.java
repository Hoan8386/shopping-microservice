package com.shoping.cartservice.command.event;

import java.util.List;

import com.shoping.commonservice.model.response.DTO.OrderItemDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRolledBackEvent {
    private String id;
    private String userId;
    private List<OrderItemDTO> listOrderItems;
}