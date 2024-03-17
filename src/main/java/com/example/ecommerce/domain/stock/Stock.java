package com.example.ecommerce.domain.stock;

import com.example.ecommerce.domain.BaseEntity;
import com.example.ecommerce.global.response.ApiCode;
import com.example.ecommerce.global.response.ApiException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "stock")
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @Column(name = "inventory_quantity")
    private Long inventoryQuantity;

    @Version
    private Long version;


    /**
     * [Foreign key]
     * */
    @Column(name = "product_id")
    private Long productId;

    /**
     * [Create Static Factory Method]
     * */
    public static Stock create(Long productId, Long inventoryQuantity) {

        return Stock.builder()
                    .productId(productId)
                    .inventoryQuantity(inventoryQuantity)
                    .build();
    }

    /**
     * [Domain Business Method]
     * */
    public void decreaseInventoryQuantity(Long inventoryQuantity) {

        if (this.inventoryQuantity <= 0) {

            throw new ApiException(ApiCode.CODE_000_0013, "재고 감소시, 수량이 0개입니다");
        }

        if (this.inventoryQuantity - inventoryQuantity < 0) {

            throw new ApiException(ApiCode.CODE_000_0014, "남은 재고보다 더 많은 수량 주문 불가");
        }

        System.out.println("************************");
        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / Before Decrease Inventory Quantity : " + this.inventoryQuantity );
        this.inventoryQuantity -= inventoryQuantity;
        System.out.println("Thread Name : " + Thread.currentThread().getName() + " / After Decrease Inventory Quantity : " + this.inventoryQuantity );
        System.out.println("=======================");
    }
}
