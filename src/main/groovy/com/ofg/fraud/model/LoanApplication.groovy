package com.ofg.fraud.model

import org.hibernate.validator.constraints.NotEmpty


class LoanApplication {
    @NotEmpty
    String firstName

    @NotEmpty
    String lastName

    @NotEmpty
    String job

    @NotEmpty
    Integer amount

    @NotEmpty
    Integer age
}
