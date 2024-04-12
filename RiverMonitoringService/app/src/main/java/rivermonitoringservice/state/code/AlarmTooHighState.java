package rivermonitoringservice.state.code;

import com.google.common.collect.Range;

import rivermonitoringservice.Constants;

public class AlarmTooHighState extends AbstractState {

    public AlarmTooHighState() {
        super();
    }

    @Override
    public void onEntry() {
        this.setMeasurementFrequency(Constants.f2);
        this.suggestValveOpeningLevel(Constants.tooHighOpeningLv);
    }

    @Override
    public void onExit() {}

    @Override
    public String getStateAsString() {
        return "Alarm - too high";
    }

    @Override
    public Range<Double> getAssociatedRange() {
        return Constants.highRange;
    }
}
