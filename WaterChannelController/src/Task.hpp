#pragma once

class Task
{
    public:
        virtual void init();
        virtual void tick(int period);
        virtual void destroy();

    protected:
        int period;
}