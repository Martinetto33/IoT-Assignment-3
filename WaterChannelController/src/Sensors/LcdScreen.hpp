#pragma once

class LcdScreen
{
    private:
        int pin[2];
    public:
        LcdScreen(int sda, int scl);
        void lcdPrint(const char* message);
        void lcdClear();
};