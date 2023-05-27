# 29CM 백엔드포지션 과제

<aside>
💡 프로젝트 사양 및 라이브러리

</aside>

- Java 11
- Gradle
- H2DB 2.1.214
- snakeyaml 2.9

<aside>
⚙ 개략적인 설계

</aside>

- 실무적인 부분으로 접근을 위한 데이터베이스사용
    - In-memory H2DB 사용
    - DB 이중화로 성능 및 가용성 증진
        - 상품의 갯수조회 및 재고 업데이트는 Master DB
        - 가장 I/O가 많이 발생할 전체 상품 조회는 Slave DB
        - 가용성을 위한 부분은 TODO
    - 동시성 제어를 위한 Master DB비관적 락 방식 적용
        - 상품 주문시 재고 확인을 위한 검색에서 MasterDB에 SELECT…FOR UPDATE로 x LOCK 획득
        - MasterDB에 재고 업데이트가 정상적으로 끝나면 SlaveDB에 똑같이 업데이트 적용
        - 모든 주문이 정상적으로 완성이 되어야만 commit, 하나라도 잘못 되었을경우 uncommit
- 최대한의 프레임워크, 라이브러리를 배제
    - 객체지향적인 설계를 더욱 고민
    - 또한, 기존 프레임워크와 라이브러리의 사용이 생산성에 얼마나 큰 도움을 주는지 다시한번 경험
      (너무 값진 경험..)
- 도메인 주도의 개발
    - 대부분의 기능을 가진것들을 객체화 하여 사용함
- 유지보수를 위한 노력
    - 대부분의 Method를 20Line이 넘지 않도록 작은 단위로 나눔
    - 일부로 주석을 최소화하여 함수명만으로 어떤 기능을 하는지 알 수 있게 네이밍에 더 신경을 씀

<aside>
👉 실행방법

</aside>

![image](https://github.com/parkilwoo/29CM/assets/56834479/c1ac3749-604a-4b9d-9247-9874236891b1)


- Main클래스의 main 함수를 실행시키면 됩니다!

<aside>
🚫 SoldOutException Test

</aside>

![image](https://github.com/parkilwoo/29CM/assets/56834479/195395f7-1bbe-4050-897d-1370d83e395f)

- 멀티쓰레드를 이용한 동시성 테스트는 제품의 갯수가 35개인 데이터를 이용하여 하나의 쓰레드에서 5개씩 구매를 실행
    - 7개의 주문내역이 출력되고 SoldOutException이 3번 나타났는지 확인

<aside>
🥲 아쉬운점

</aside>

- 작업 시간이 조금 부족 하였음
    - 과제를 전달받은 시간이 회사가 많이 바쁜 시기여서 시간에 쫓김
    - 데이터베이스 구현에 너무 나도 많은 시간을 소요
        - 그에 비해, 만족스럽지 못한 구현
- 레디스 연동을 머릿속으로 설계했는데, 구현하지 못함
    - 전체 목록 조회가 가장 시간이 오래 걸릴것으로 생각되어 요청시 redis를 두어서 가장 최근의 cache hit를 유도
        - cache miss시 slave db에서 조회 후 redis에 적재
        - 주문으로 인해 재고가 업데이트 될 때마다, redis 데이터를 sync
