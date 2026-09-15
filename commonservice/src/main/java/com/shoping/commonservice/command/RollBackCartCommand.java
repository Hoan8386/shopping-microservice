package com.shoping.commonservice.command;

import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.shoping.commonservice.model.response.DTO.OrderItemDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RollBackCartCommand {
      @TargetAggregateIdentifier
      private String id;

      private String userId;

      private List<OrderItemDTO> listOrderItems;
}
