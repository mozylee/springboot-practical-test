package practice.practicaltest.kiosk.beverage.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import practice.practicaltest.kiosk.beverage.Beverage;

@Getter
@NoArgsConstructor
public class Americano implements Beverage {

    private String name = "아메리카노";

    private int price = 4500;

}
