package practice.practicaltest.kiosk;

import practice.practicaltest.kiosk.beverage.impl.Americano;
import practice.practicaltest.kiosk.beverage.impl.Latte;

public class CafeKioskRunner {

    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println("아메리카노 추가~");

        cafeKiosk.add(new Latte());
        System.out.println("라떼 추가~");

        System.out.println("cafeKiosk = " + cafeKiosk);
        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("totalPrice = " + totalPrice);
    }

}
