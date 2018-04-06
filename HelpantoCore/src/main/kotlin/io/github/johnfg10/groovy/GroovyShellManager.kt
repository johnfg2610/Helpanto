package io.github.johnfg10.groovy

import groovy.lang.Binding
import groovy.lang.GroovyShell
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.CompilationCustomizer
import org.kohsuke.groovy.sandbox.GroovyValueFilter
import org.kohsuke.groovy.sandbox.SandboxTransformer

public class GroovyShellManager(val binding: Binding = Binding()) : AutoCloseable {

    val compConfig: CompilerConfiguration = CompilerConfiguration()
    lateinit var shell: GroovyShell

    init {
        compConfig.addCompilationCustomizers(SandboxTransformer())
        shell = GroovyShell(binding, compConfig)
        GroovyDenyAllFilter().register()
        //DenyAll().register()
        //println(shell.evaluate("""return "testing""""))

    }

    public fun eval(expression: String) : Any? {
        return shell.evaluate(expression)
    }

    override fun close() {
        shell.resetLoadedClasses()
    }

    companion object {
        public open class DenyAll : GroovyValueFilter() {
            override fun filter(o: Any?): Any {
                throw SecurityException("Denied!")
            }
        }
    }
}