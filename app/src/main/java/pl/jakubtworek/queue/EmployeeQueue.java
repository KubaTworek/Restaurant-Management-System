package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.SimpleEmployee;

interface EmployeeQueue {
    SimpleEmployee get();
    int size();
    void add(SimpleEmployee employee);
}
