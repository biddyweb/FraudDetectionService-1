package com.ofg.fraud

import com.ofg.fraud.model.LoanApplication
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

import javax.validation.constraints.NotNull

@Slf4j
@RestController
@RequestMapping('/api')
class FraudController {

    @Autowired
    ServiceRestClient serviceRestClient;


    @RequestMapping(value = "/loanApplication/{loanApplicationId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    void verifyLoanApplication(
            @PathVariable @NotNull String loanApplicationId,
            @RequestBody LoanApplication loanApplication) {


        def fraudStatus = "GREEN";

        if (loanApplication.age <= 21 || loanApplication.age >= 65) {
            fraudStatus = "RED"
        }

        def json = new JsonBuilder()
        def root = json firstName: loanApplication.firstName, lastName: loanApplication.lastName, job: loanApplication.job, amount: loanApplication.amount, fraudStatus: fraudStatus

        serviceRestClient.forService('decision-maker').put()
                .onUrl('/api/loanApplication/' + loanApplicationId)
                .body(root)
                .withHeaders()
                .contentTypeJson()
                .andExecuteFor()
                .aResponseEntity()
                .ofType(Object).statusCode
    }


}
