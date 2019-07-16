package mimic.provider

import mimic.mountebank.provider.report.ProviderReportBuilder
import mimic.mountebank.provider.report.StandardProviderReport
import spock.lang.Specification

class ProviderReportSpec extends Specification {

    def "creating a ProviderReport builder and calling build returns a ProviderReport"() {
        given:
        def reportBuilder = new ProviderReportBuilder()

        when:
        def report = reportBuilder.build()

        then:
        report.class == StandardProviderReport.class
    }

    def "creating a provider request event is reflected in the report"() {
        given:
        def reportBuilder = new ProviderReportBuilder()

        when:
        reportBuilder.createProviderRequestEvent("key", "event")
        def report = reportBuilder.build()

        then:
        report.getProviderRequestEvents() == ["key":"event"]
    }

    def "creating a provider request event is reflected in the report string"() {
        given:
        def reportBuilder = new ProviderReportBuilder()

        when:
        reportBuilder.createProviderRequestEvent("key", "event")
        def report = reportBuilder.build()

        then:
        println report.toString()
        report.toString() != ""
    }

    def "creating a provider request and response event is reflected in the report"() {
        given:
        def reportBuilder = new ProviderReportBuilder()

        when:
        reportBuilder.createProviderRequestEvent("key0", "event0")
        reportBuilder.createProviderResponseEvent("key1", "event1")
        def report = reportBuilder.build()

        then:
        report.getProviderRequestEvents() == ["key0":"event0"]
        report.getProviderResponseEvents() == ["key1":"event1"]
    }

    def "creating a provider request and response event is reflected in the report string"() {
        given:
        def reportBuilder = new ProviderReportBuilder()

        when:
        reportBuilder.createProviderRequestEvent("key0", "event0")
        reportBuilder.createProviderResponseEvent("key1", "event1")
        def report = reportBuilder.build()

        then:
        println report.toString()
        report.toString().contains("request") == true
        report.toString().contains("Response") == true
    }
}
