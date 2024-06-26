package rivermonitoringservice.state.code;

import com.google.common.collect.Range;

import rivermonitoringservice.Constants;

public class AlarmTooHighCriticState extends AbstractState {

    public AlarmTooHighCriticState() {
        super();
    }

    @Override
    public void onEntry() {
        this.setMeasurementFrequency(Constants.f2);
        this.suggestValveOpeningLevel(Constants.tooHighCriticOpeningLv);
    }

    @Override
    public void onExit() {}

    @Override
    public String getStateAsString() {
        return "ALARM - TOO HIGH CRITIC!";
    }

    @Override
    public Range<Double> getAssociatedRange() {
        return Constants.criticalRange;
    }
}
