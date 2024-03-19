#pragma once

/*
    A template class for storing different types of States.
*/
template <class X> class AbstractFSM {
    private:
        X current_state;
    public:
        AbstractFSM(X initial_state);
        X getCurrentState();
        void setCurrentState(X current_state);
};

/* A constructor for an Abstract FSM. */
template <typename X> 
AbstractFSM<X>::AbstractFSM(X initial_state) {
    this->current_state = initial_state;
}

/* A method that returns the current state. */
template <typename X> 
X AbstractFSM<X>::getCurrentState() {
    return this->current_state;
}

/* A method that sets the current state to the one specified by the user. */
template <typename X> 
void AbstractFSM<X>::setCurrentState(X current_state) {
    this->current_state = current_state;
}
