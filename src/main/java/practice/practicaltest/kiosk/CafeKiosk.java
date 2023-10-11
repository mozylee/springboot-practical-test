package practice.practicaltest.kiosk;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import practice.practicaltest.kiosk.beverage.Beverage;
import practice.practicaltest.kiosk.order.Order;

@Getter
public class CafeKiosk {

    private static final LocalTime CAFE_OPEN_TIME = LocalTime.of(8, 0, 0);

    private static final LocalTime CAFE_CLOSE_TIME = LocalTime.of(22, 0, 0);

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(Beverage beverage, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("음료는 1잔 이상 주문 가능합니다.");
        }

        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        return beverages.stream()
                        .mapToInt(Beverage::getPrice)
                        .sum();
    }

    @Override
    public String toString() {
        return beverages.toString();
    }

    public Order createOrder(LocalDateTime currentDateTime) {
        LocalTime localTime = currentDateTime.toLocalTime();
        if (localTime.isBefore(CAFE_OPEN_TIME) || localTime.isAfter(CAFE_CLOSE_TIME)) {
            throw new IllegalArgumentException("운영 시간이 아닙니다. 운영 시간에 주문해주세요.");
        }

        return new Order(beverages, currentDateTime);
    }

}
