#include "AbstractTask.hpp"
#include "Sensors/SonarSensor.hpp"

class SonarMeasurementTask : AbstractTask {
    private:
        SonarSensor sonar;
        // Queue<float> measurementsQueue;
    public:
        SonarMeasurementTask();
        void measureWaterLevel();
        float getLastMeasurement();
        bool isMeasurementAvailable();
};


