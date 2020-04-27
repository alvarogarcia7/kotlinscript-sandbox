#!/bin/bash

//usr/bin/env echo '
/**** BOOTSTRAP kscript ****\'>/dev/null
command -v kscript >/dev/null 2>&1 || curl -L "https://git.io/fpF1K" | bash 1>&2
exec kscript $0 "$@"
\*** IMPORTANT: Any code including imports and annotations must come after this line ***/
@file:DependsOn("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.9.4")
@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0.rc1")
@file:EntryPoint("XmlValidator")

package skimmer

import DependsOn
import EntryPoint
import MavenRepository
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.nio.file.Files
import java.nio.file.Paths

data class Mutation(
        var sourceFile: String
//        var status: String,
//        var numberOfTestsRun: String
)

class XmlValidator() {

    fun run(path: java.nio.file.Path): Mutation{
        val mutation = xmlMapper.readValue(path.toFile(), Mutation::class.java)

        if(mutation.sourceFile == ""){
            throw java.lang.IllegalArgumentException("empty source file")
        }
        return mutation
    }

    companion object {
        val xmlMapper = XmlMapper().findAndRegisterModules().enable(SerializationFeature.INDENT_OUTPUT)

        @JvmStatic
        fun main(args: Array<String>) {
            val path = Paths.get(args[0]).toAbsolutePath()

            val validated = XmlValidator().run(path)

            val asString: Any = xmlMapper.writeValueAsString(validated)
            println(asString)
        }
    }
}
