## Widget Generation Pre-Requisites ##

In order for the KB2 application user interface to work, projects need to have suitable configuration with specialist Eclipse project facets, as follows:

1) All your src file directories should generally be seen as Java build path source folders, in particular, your web code directories

2) Select Eclipse project "Properties..Project Facets"

3) Click the check boxes for the following project facets (if not already checked):

        Dynamic Web Module
        Java
        Vaadin Plugin for Eclipse
                
4) Optional configuration questions may be answered:

        * The project folder designated to contain dynamic web content. The default is to create the "WebContent" subfolder in the root folder of the project and is probably fine. In the same configuration screen, the checkbox to include a web.xml should be clicked ("on")
        
        * Whether or not you are creating a new Vaadin project. Unless you are creating your project from scratch (probably not?), you should also ensure that the "Create New Project Template" checkbox is left **unchecked**, when the configuration screen for the Vaadin plug-in is presented.
        
        * If you need to turn on the Java facet, root Eclipse src and build directories are specified
        
5) Your project (e.g. Gradle) build file(s) should probably be updated to include the vaadin dependencies as required, e.g. (for Gradle, a representative code version, 7.5.10, shown - your choice may differ...):

    compile 'com.vaadin:vaadin-server:7.5.10'
    compile 'com.vaadin:vaadin-themes:7.5.10'
    compile 'com.vaadin:vaadin-client:7.5.10'
    compile 'com.vaadin:vaadin-client-compiled:7.5.10'
    compile 'com.vaadin:vaadin-client-compiler:7.5.10'

Note:

* If you get an error while installing the facet, and need to start again, to restart, you likely need to delete files in the Eclipse .settings directory called **"com.vaadin.integration.eclipse.prefs"** and **"org.eclipse.wst.common.project.facet.core.xml"** to render the project "facet naive". However, in such case, you may be asked to reconfigure facets like the Java facet. You may also need to delete any 'WebContent' entry in the file **"org.eclipse.wst.common.component"** and **.jsdtscope**. You should also delete any WebContent directory previously generated.

##Generating Widgetsets (and new Vaadin Themes?)##

The Eclipse toolbar has a tool looking something like a pair of one large and one small, black inter-meshed
 gears. This is the Vaadin widget generator tool, with three options:
 
     Compile Widgetset
     Compile Theme
     Compile Widgetset and Theme

Selecting your project (root) and one of these options (generally, just "Compile Widgetset") starts the compilation process.
    
## Troubleshooting ##

Build dependencies may be tricky. In some cases, you may get the following error:

Unexpected internal compiler error
java.lang.IncompatibleClassChangeError: class com.google.gwt.dev.javac.BytecodeSignatureMaker$CompileDependencyVisitor has interface org.objectweb.asm.ClassVisitor as super class

See https://vaadin.com/forum#!/thread/9316748 for information on the source of this error. Using "gradle dependencyInsight --dependency asm" may reveal dependency conflicts of the GWT asm dependency with an older version of 'asm' used by jetty(?). The following code, added to your main Gradle build file, **could** resolve this error:

	configurations.all {
	    resolutionStrategy.eachDependency {
	        if(it.requested.name == 'org.objectweb.asm') {
	            it.useTarget 'org.ow2.asm:asm:5.0.3'
	        }
	    }
	}
