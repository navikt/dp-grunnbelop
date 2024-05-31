package no.nav.dagpenger.grunnbelop.features

import io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME
import io.cucumber.core.options.Constants.PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@SelectClasspathResource("no/nav/dagpenger/grunnbelop/features")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME,
    value = "pretty, html:build/reports/cucumber.html",
)
@ConfigurationParameter(
    key = PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME,
    value = "false",
)
class RunCucumberTest
