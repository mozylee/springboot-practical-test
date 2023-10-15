package practice.practicaltest.kiosk.beverage.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import practice.practicaltest.kiosk.beverage.Beverage;

@Getter
@NoArgsConstructor
public class Latte implements Beverage {

    private String name = "라떼";

    private int price = 5500;

}
