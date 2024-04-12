package rivermonitoringservice.state.code;

import com.google.common.collect.Range;

import rivermonitoringservice.Constants;

public class AlarmTooLowState extends AbstractState {

    public AlarmTooLowState() {
        super();
    }

    @Override
    public void onEntry() {
        this.setMeasurementFrequency(Constants.f1);
        this.suggestValveOpeningLevel(Constants.lowOpeningLv);
    }

    @Override
    public void onExit() {}

    @Override
    public String getStateAsString() {
        return "Alarm - too low";
    }

    @Override
    public Range<Double> getAssociatedRange() {
        return Constants.lowRange;
    }
}
