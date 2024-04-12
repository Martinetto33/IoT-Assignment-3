package rivermonitoringservice.state.code;

import com.google.common.collect.Range;

import rivermonitoringservice.Constants;

public class NormalState extends AbstractState {

    public NormalState() {
        super();
    }

    @Override
    public void onEntry() {
        this.setMeasurementFrequency(Constants.f1);
        this.suggestValveOpeningLevel(Constants.normalOpeningLv);
    }

    @Override
    public void onExit() {}

    @Override
    public String getStateAsString() {
        return "Normal";
    }

    @Override
    public Range<Double> getAssociatedRange() {
        return Constants.normalRange;
    }
}
