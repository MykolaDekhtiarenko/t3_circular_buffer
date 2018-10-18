package com.epam.training;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ArrayCircularBuffer<T> implements CircularBuffer<T> {
    private T[] buffer;
    private int writePointer = 0;
    private int readPointer = 0;

    public ArrayCircularBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size should be more then 0.");
        }
        this.buffer = (T[]) new Object[size];
    }

    public ArrayCircularBuffer() {
        this(10);
    }

    @Override
    public void put(T t) {
        if (writePointer == readPointer && !isEmpty()) {
            throw new UnsupportedOperationException("Buffer is full!");
        }
        buffer[writePointer] = t;
        writePointer = nextPointerPosition(writePointer);
    }

    @Override
    public T get() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Buffer is empty!");
        }
        T res = buffer[readPointer];
        readPointer = nextPointerPosition(readPointer);
        return res;
    }

    @Override
    public Object[] toObjectArray() {
        Object[] objects = new Object[size()];
        if (isFlipped()) {
            System.arraycopy(buffer, readPointer, objects, 0, buffer.length - readPointer);
            System.arraycopy(buffer, 0, objects, objects.length, writePointer);

        } else {
            System.arraycopy(buffer, readPointer, objects, 0, size());
        }
        return objects;
    }

    @Override
    public T[] toArray() {
        return (T[]) toObjectArray().clone();
    }

    @Override
    public List<T> asList() {
        return Arrays.asList(toArray());
    }

    @Override
    public void addAll(List<? extends T> toAdd) {
        if (buffer.length < size() + toAdd.size()) {
            throw new UnsupportedOperationException("You tried to add to many elements.");
        }
        toAdd.forEach(this::put);
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        List<T> elements = asList();
        elements.sort(comparator);
        buffer = (T[]) elements.toArray();
        //MAINTAIN POINTERS POSITION
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    private int nextPointerPosition(int pointer) {
        if (pointer + 1 < buffer.length) {
            return pointer + 1;
        }
        return 0;
    }

    private boolean isFlipped() {
        return readPointer > writePointer;
    }

    private int size() {
        if (isFlipped()) {
            return buffer.length - readPointer + writePointer;
        } else {
            return writePointer - readPointer;
        }
    }
}
