#pragma once
#include <assert.h>

/**
 * IMPORTANT DISCLAIMER: these classes are not optimised for storing String values,
 * since there are some low-level mechanisms that need to be taken care of (for example,
 * the fact that the size of the String is generally unknown; this can cause writings to
 * reserved memory locations, causing abnormal behaviours).
 * 
 * A solution might be using the "SafeString" library, as suggested here:
 * https://forum.arduino.cc/t/esp32-guru-meditation-error-core-1-paniced-storeprohibited-exception-was-unhandled/1003631/8
*/

/**
 * A class modelling a generic Queue element.
*/
template <class X> 
class QueueElement {
    private:
        X element; // the element contained in this QueueElement
        QueueElement* next; // the next QueueElement in this Queue.
    public:
        QueueElement(X element);
        ~QueueElement(); // destructor
        X getElement();
        QueueElement<X>* getNext(); // return pointer to next element
        void assignValueToNext(QueueElement<X>* queueElement); // used in the add() method of the Queue
};

/* Implementation of the generic constructor of a QueueElement. */
template <typename X> 
QueueElement<X>::QueueElement(X element) {
    this->element = element;
    this->next = nullptr;
}

/* Implementation of the generic getElement() method of a QueueElement. */
template <typename X> 
X QueueElement<X>::getElement() {
    return this->element;
}

/* Implementation of the generic getNext() method of a QueueElement. */
template <typename X>
QueueElement<X>* QueueElement<X>::getNext() {
    return this->next;
}

template <typename X>
void QueueElement<X>::assignValueToNext(QueueElement<X>* queueElement) {
    this->next = queueElement;
}

/* Implementation of the generic destructor of a QueueElement. */
template <typename X>
QueueElement<X>::~QueueElement() {
    delete this;
}

/**
 * A class modelling a generic Queue, useful for holding various data
 * types in a Queue data structure. Users should instantiate
 * objects of this class, and should avoid directly using
 * QueueElements.
*/
template <class X> 
class Queue {
    private:
        QueueElement<X>* queueHead;
        int size;
    public:
        Queue();
        ~Queue(); // destructor
        void add(X element);
        bool isEmpty();
        X poll(); // warning: do not call when the Queue is empty!
};

/* Implementation of the generic constructor of a Queue. */
template <typename X>
Queue<X>::Queue() {
    this->size = 0;
    this->queueHead = nullptr;
}

/**
 * Implementation of the generic add() method of a Queue. Elements must be added at the end of a Queue
 * to respect the intent of this data structure.
*/
template <typename X>
void Queue<X>::add(X element) {
    QueueElement<X>* queueElement = new QueueElement<X>(element); // the new QueueElement to be added
    if (this->queueHead == nullptr) {
        // if this is the first element to be added
        this->queueHead = queueElement;
    } else {
        QueueElement<X>* currentElement = this->queueHead; // a pointer used to iterate over the queued elements
        /* The element with its 'next' field pointing to null has to be found. */
        while (currentElement->getNext() != nullptr) {
            currentElement = currentElement->getNext();
        }
        currentElement->assignValueToNext(queueElement); // insertion of the new element at the end of the queue
    }
    this->size++;
}

/* Implementation of the generic isEmpty() method of a Queue. */
template <typename X>
bool Queue<X>::isEmpty() {
    return this->size == 0;
}

/* Implementation of the generic poll() method of a Queue. */
template <typename X>
X Queue<X>::poll() {
    assert(!this->isEmpty());
    /* Extract the first element in this Queue. */
    QueueElement<X>* polledElement = this->queueHead;
    /* Ensure that queueHead points to the next element in queue, which would be
    nullptr if the Queue only contained one element. */
    this->queueHead = this->queueHead->getNext();

    /* Save the return value of the polled element. */
    X result = polledElement->getElement();
    this->size--; // decrease the size of the queue

    return result;
}

/* Implementation of the generic destructor of a Queue. */
template <typename X>
Queue<X>::~Queue() {
    if (!this->isEmpty()) {
        QueueElement<X>* currentElement = this->queueHead; // a pointer used to iterate over the queued elements
        while (currentElement->getNext() != nullptr) {
            this->queueHead = this->queueHead->getNext(); // move the head to the next element
            delete currentElement;
            currentElement = this->queueHead; // assign currentElement to the same element pointed by the head
        }
        delete currentElement; // delete the last element
    }
    delete this;
}