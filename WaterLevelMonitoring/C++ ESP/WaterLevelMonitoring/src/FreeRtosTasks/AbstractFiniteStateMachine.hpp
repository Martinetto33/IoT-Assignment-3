#pragma once

/*
    A template class for storing different types of States.
*/
template <class X> class AbstractFiniteStateMachine {
    private:
        X current_state;
    public:
        AbstractFiniteStateMachine(X initial_state);
        X getCurrentState();
        void setCurrentState(X current_state);
};

/* A constructor for an Abstract FSM. */
template <typename X> 
AbstractFiniteStateMachine<X>::AbstractFiniteStateMachine(X initial_state) {
    this->current_state = initial_state;
}

/* A method that returns the current state. */
template <typename X> 
X AbstractFiniteStateMachine<X>::getCurrentState() {
    return this->current_state;
}

/* A method that sets the current state to the one specified by the user. */
template <typename X> 
void AbstractFiniteStateMachine<X>::setCurrentState(X current_state) {
    this->current_state = current_state;
}
