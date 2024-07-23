package com.spoonsors.spoonsorsserver.entity.payment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApproveRequestPayDto {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private String item_name;
    private String item_code;
    private int quantity;
    private String created_at;
    private String approved_at;
    private String payload;
    private PayHistoryDto pay_history;
}
