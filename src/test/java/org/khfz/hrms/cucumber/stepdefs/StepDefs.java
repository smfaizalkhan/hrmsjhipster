package org.khfz.hrms.cucumber.stepdefs;

import org.khfz.hrms.HrmsApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = HrmsApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
