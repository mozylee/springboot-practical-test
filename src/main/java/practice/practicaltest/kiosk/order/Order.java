package practice.practicaltest.kiosk.order;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import practice.practicaltest.kiosk.beverage.Beverage;

@Getter
@RequiredArgsConstructor
public class Order {

    private final List<Beverage> beverages;

    private final LocalDateTime orderDateTime;

}
