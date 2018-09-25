package org.example.domainmodel.generator
 
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import org.example.domainmodel.domainmodel.Entity
import org.example.domainmodel.domainmodel.Feature
import org.eclipse.xtext.naming.IQualifiedNameProvider
 
import com.google.inject.Inject
 
class DomainmodelGenerator extends AbstractGenerator {
 
    @Inject extension IQualifiedNameProvider
 
    override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
        for (e : resource.allContents.toIterable.filter(Entity)) {
            fsa.generateFile(
                e.fullyQualifiedName.toString("/") + ".sql", e.compile)
        }
    }
    
    def compile(Entity e) ''' 
        CREATE TABLE «e.name» «IF e.superType !== null»«e.superType.fullyQualifiedName» «ENDIF»(
            «FOR f : e.features»
                «f.compile»«IF (e.features.get(e.features.size()-1) != f)»,«ENDIF»
            «ENDFOR»
        ) «IF e.superType !== null»inherits («e.superType.name»)«ENDIF»;
    '''
 
    def compile(Feature f) '''
        «f.name» «f.type.fullyQualifiedName» «IF f.not !== null»not «ENDIF»null «IF f.key !== null»«f.key.toLowerCase» key«ENDIF»
    '''
}