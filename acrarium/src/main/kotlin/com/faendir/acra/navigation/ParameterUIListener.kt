package com.faendir.acra.navigation

import com.faendir.acra.service.DataService
import com.faendir.acra.util.catching
import com.vaadin.flow.server.UIInitEvent
import com.vaadin.flow.server.UIInitListener
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import java.util.function.Supplier
import kotlin.reflect.full.createInstance

@Component
class ParameterUIListener(private val applicationContext: GenericApplicationContext, private val dataService: DataService) : UIInitListener {
    private val nameCache = mutableSetOf<String>()
    override fun uiInit(uiInitEvent: UIInitEvent) {
        uiInitEvent.ui.addBeforeEnterListener { event ->
            event.routeParameters.parameterNames.forEach {
                val annotation = AnnotationUtils.findAnnotation(event.navigationTarget, ParseParameter::class.java)
                val parameter = event.routeParameters.get(it).orElseThrow()
                synchronized(nameCache) {
                    nameCache.add(it)
                }
                if (annotation != null) {
                    registerParsedParameter(annotation.value.createInstance(), it, parameter)
                } else {
                    applicationContext.registerBean(it, String::class.java, parameter)
                }
            }
        }
        uiInitEvent.ui.addAfterNavigationListener {
            synchronized(nameCache) {
                nameCache.forEach { catching { applicationContext.removeBeanDefinition(it) } }
                nameCache.clear()
            }
        }
    }

    private fun <T : Any> registerParsedParameter(parser: ParameterParser<T>, name: String, parameter: String) {
        val parsed = parser.parse(dataService, parameter) ?: throw IllegalArgumentException()
        applicationContext.registerBean(name, parser.type.java, Supplier { parsed })
    }
}