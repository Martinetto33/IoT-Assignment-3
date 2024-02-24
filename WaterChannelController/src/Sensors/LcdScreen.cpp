#include "LcdScreen.hpp"
#include <Arduino.h>
#include <LiquidCrystal_I2C.h> 

LiquidCrystal_I2C lcd = LiquidCrystal_I2C(0x27,20,4); 

LcdScreen::LcdScreen(int sda, int sld) {
    pin[0] = sda;
    pin[1] = sld;
    lcd.init();
    lcd.backlight();
}

void LcdScreen::lcdPrint(const char* message) {
    lcd.clear();
    lcd.setCursor(2,1);
    lcd.print(message);
}

void LcdScreen::lcdClear() {
    lcd.clear();   
}