package rivermonitoringservice.state.code;

import com.google.common.collect.Range;

import rivermonitoringservice.Constants;
import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;

public class PreAlarmTooHighState extends AbstractState {

    public PreAlarmTooHighState(RiverMonitoringServiceFSM fsm) {
        super(fsm);
    }

    @Override
    public void onEntry() {
        this.setMeasurementFrequency(Constants.f2);
        this.suggestValveOpeningLevel(Constants.preTooHighOpeningLv);
    }

    @Override
    public void onExit() {}

    @Override
    public String getStateAsString() {
        return "Pre-alarm - too high";
    }

    @Override
    public Range<Double> getAssociatedRange() {
        return Constants.preHighRange;
    }
}
