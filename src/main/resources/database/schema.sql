create table PRODUCT
(
    PRODUCT_ID  integer       not null comment '상품고유번호',
    NAME        varchar (100) not null comment '상품명',
    PRICE       integer       not null comment '상품가격',
    STOCK_COUNT smallint      not null comment '재고수량'
    primary key (PRODUCT_ID)
)
    comment '상품테이블';