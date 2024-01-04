package me.bongle.ksp.slf4j

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Slf4jProcessor(val env: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Slf4j::class.java.name)
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(Slf4jProcessorVisitor(), Unit) }
        return ret
    }

    inner class Slf4jProcessorVisitor : KSVisitorVoid() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.packageName.asString()
            val ksType = classDeclaration.asType(emptyList())

            val fileSpec = FileSpec.builder(
                packageName = packageName,
                fileName = classDeclaration.simpleName.asString() + "Ext"
            ).apply {
                val className = ksType.toClassName()
                val loggerName = "_${className.simpleName.replaceFirstChar { it.lowercase() }}Logger"
                addProperty(
                    PropertySpec.builder(loggerName, Logger::class.java)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer("%T.getLogger(%T::class.java)", LoggerFactory::class.java, className)
                        .build()
                )
                addProperty(
                    PropertySpec.builder("logger", Logger::class.java)
                        .receiver(className)
                        .getter(
                            FunSpec.getterBuilder()
                                .addStatement("return $loggerName")
                                .build()
                        )
                        .build()
                )
            }.build()

            fileSpec.writeTo(codeGenerator = env.codeGenerator, aggregating = false)
        }
    }

}


class Slf4jProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment) = Slf4jProcessor(environment)

}
