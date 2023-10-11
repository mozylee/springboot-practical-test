package practice.practicaltest.kiosk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice.practicaltest.kiosk.beverage.Beverage;
import practice.practicaltest.kiosk.beverage.impl.Americano;
import practice.practicaltest.kiosk.beverage.impl.Latte;
import practice.practicaltest.kiosk.order.Order;

class CafeKioskTest {

    private CafeKiosk cafeKiosk;

    @BeforeEach
    public void beforeEach() {
        cafeKiosk = new CafeKiosk();
    }

    @Test
    @DisplayName("add: 정상 케이스")
    public void add() throws Exception {
        // given
        Beverage americano = new Americano();

        // when
        cafeKiosk.add(americano);

        // then
        assertThat(cafeKiosk.getBeverages()).hasSize(1)
                                            .contains(americano);
    }

    @Test
    @DisplayName("add: 여러 잔 정상 케이스")
    public void addMany() throws Exception {
        // given
        Beverage americano = new Americano();
        int count = 2;

        // when
        cafeKiosk.add(americano, count);

        // then
        assertThat(cafeKiosk.getBeverages()).hasSize(count)
                                            .contains(americano);
    }

    @Test
    @DisplayName("add: 여러 잔 실패 케이스-count <= 0")
    public void addMany_2() throws Exception {
        // given
        Beverage americano = new Americano();
        int count = 0;

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cafeKiosk.add(americano, count));

        // then
        assertThat(exception.getMessage()).isEqualTo("음료는 1잔 이상 주문 가능합니다.");
    }

    @Test
    @DisplayName("remove: 정상 케이스")
    public void remove() throws Exception {
        // given
        Americano americano = new Americano();

        // when
        cafeKiosk.add(americano);
        cafeKiosk.remove(americano);

        // then
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    @DisplayName("clear: 정상 케이스")
    public void clear() throws Exception {
        // given
        List<Beverage> beverages = List.of(new Americano(), new Latte());

        // when
        beverages.forEach(cafeKiosk::add);
        cafeKiosk.clear();

        // then
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    @DisplayName("createOrder: 정상 케이스")
    void createOrder() {
        // given
        Beverage americano = new Americano();
        LocalDateTime currentDateTime = LocalDateTime.of(2023, 1, 1, 9, 0, 0);

        // when
        cafeKiosk.add(americano);
        Order order = cafeKiosk.createOrder(currentDateTime);

        // then
        assertThat(order.getBeverages()).hasSize(1)
                                        .contains(americano);
    }

    @Test
    @DisplayName("createOrder: 예외 케이스-영업시간 전")
    void createOrder1() {
        // given
        Beverage americano = new Americano();
        LocalDateTime currentDateTime = LocalDateTime.of(2023, 1, 1, 7, 59, 59);

        // when
        cafeKiosk.add(americano);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> cafeKiosk.createOrder(currentDateTime));

        // then
        assertThat(exception.getMessage()).isEqualTo("운영 시간이 아닙니다. 운영 시간에 주문해주세요.");
    }

    @Test
    @DisplayName("createOrder: 예외 케이스-영업시간 후")
    void createOrder2() {
        // given
        Beverage americano = new Americano();
        LocalDateTime currentDateTime = LocalDateTime.of(2023, 1, 1, 22, 0, 1);

        // when
        cafeKiosk.add(americano);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> cafeKiosk.createOrder(currentDateTime));

        // then
        assertThat(exception.getMessage()).isEqualTo("운영 시간이 아닙니다. 운영 시간에 주문해주세요.");
    }

    @Test
    @DisplayName("calculateTotalPrice: 정상 케이스")
    public void calculateTotalPrice() throws Exception {
        // given
        List<Beverage> beverages = List.of(new Americano(), new Americano(), new Latte(), new Latte());
        int expectedTotalPrice = beverages.stream()
                                          .mapToInt(Beverage::getPrice)
                                          .sum();

        // when
        beverages.forEach(cafeKiosk::add);
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(expectedTotalPrice);
    }

}