package com.project.smartbuy.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailListResponse {
  private List<OrderDetailResponse> orderDetails;
}
