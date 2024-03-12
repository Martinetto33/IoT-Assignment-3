#include "AbstractTask.hpp"
#include "Sensors/SonarSensor.hpp"
#include "Queue/Queue.hpp"

class SonarMeasurementTask : AbstractTask {
    private:
        SonarSensor sonar;
        Queue<float> measurementsQueue;
        int measurementFrequency;
    public:
        SonarMeasurementTask();
        /* Virtual methods have to be overridden inside class definition: 
        https://stackoverflow.com/questions/15117591/why-is-inherited-member-not-allowed
        */
        void pause() override;
        void resume() override;
        void measureWaterLevel();
        float getLastMeasurement();
        bool isMeasurementAvailable();
        void setMeasurementFrequency(int newMeasurementFrequency);
        int getMeasurementFrequency();
};


