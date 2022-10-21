package com.example.asus;

import java.util.Random;


class RandomUtils {

    static int getRandomValue() {
        Random rand = new Random();
        return Math.abs(rand.nextInt());
    }
}
