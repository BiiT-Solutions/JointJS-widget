# Introduction

## About this project

This project is an integration of the [JoinJS](https://github.com/clientIO/joint) library
into [Vaadin](https://vaadin.com/) Framework. It generates a Widget that can be used as any other Vaadin component in
any
application based on this framework.

## JoinJS

JoinJS is a library based on JavaScript for diagram generation. The most important feature of JoinJS is that allows some
interactions between the diagrams and the user. Has several featured but here we will only use a small
set of them.

We are using version JointJS v0.9.0 on this project, but currently,
version [3.6.5](https://github.com/clientIO/joint/releases/tag/v3.6.5) of this library is available.

## Vaadin

Vaadin is a web app development framework for Java that helps you to generate UI design and components. The main feature
of Vaadin is that you can generate web based applications only using Java language.

This project is based on Vaadin 7.

# The Project

As described before, this project is a widget that can aggregated to a Vaadin web application to extends its features.
That means that the final scope is that can be included in any Vaadin application.

## Project Structure

The project is split into two different parts:

- Client: that is the widget itself that will be used later on a different system.
- Demoproject: an option to run this project as a standalone application. It helps to keep it simple and easy to test.

## Requirements

As is a Java based application, we need to install Java JDK and some related tools to make it works and easy to compile.
The required tools are:

- Java VM JDK. We are using the old Java 8 VM. Personally, I use now the OpenJDK 8u322-b06-1.
- Maven. at least version 3.6. Maven will use the current scripts of the project for compilation purpose. It will help
  you to compile and execute the project without having knowledge of what are you doing. All Maven configuration is
  defined on the `pom.xml` file of the project.

### Dependencies

The project only has as external dependency the Vaadin framework. It will be downloaded automatically when Maven is
executed.

# Adding/Updating functionality

As a developer, probably you want to maintain/update/upgrade this project. Here you will find some clues that will help
you on the process.

## File Structure

`src/main/webapp/VAADIN/dist/` Here all original JoinJS Java Scripts files are stored. If you want to update JoinJS
version, you must replace the files on this folder by the new ones containing the new version.

`src/main/webapp/VAADIN/lib/` Other JavaScript libraries used on this project.

`src/main/webapp/VAADIN/src/` This is the most important folder of the project, as here is stored the implementation of
the connectors between Vaadin and JoinJS (`main.js`) but also includes the nodes definition (`dataTypes.js`
and `inspector.js`) with their properties and icons. The files stored on this folder must be updated if the JoinJS
version is changed to fit any change introduced by JoinJS.

`src/main/java/com/biit/jointjs/diagram/builder/client/` Stores all Java files of the project.

## Project Architecture

The idea of this project is to have a 3rd party library based on JavaScript (JoinJS) and include it on Vaadin Framework.
For this purpose we need to generate some connectors that link Vaadin actions to JoinJS functions. We will need
connectors for any action that must be sent to JoinJS, but also connectors where JoinJS must report changes to Vaadin.
The connectors are based on GWT (Google Web Toolkit) as is how Vaadin works.

To define a connector we need to:

### Use a new JoinJS feature

Define a function on `src/main/webapp/VAADIN/src/main.js` Here we will define what the action will do on JoinJs, using
JavaScript language and JoinJS features. It will be good if first you take a look on JoinJS examples of how to use the
library. Note that any action on JavaScript that is not directly related to JoinJS also can be included here.
The `src/main/webapp/VAADIN/src/main.js` must be in line with the `src/main/webapp/VAADIN/dist/join.js` file, and any
change on the second one must be reflected on the first one.

### Create a connector to link Java actions to be reflected on JoinJS

On Java, we need to generate a connector. For this purpose we have to modify the next classes:

`src/main/java/com/biit/jointjs/diagram/builder/client/ui/VDiagramBuider.java` that has all the conversions from Java
functions to JavaScript functions. And also includes all JavaScript events to Java listeners to handle any action that
must be notified to the backend. The Java listeners are stored
on `src/main/java/com/biit/jointjs/diagram/builder/client/ui/events/` and will be used later on the code as a usual
listener/event architecture. Remember that all Event Handles are based on GWT.

After a listener is generated, we link the GWT code to the Java Code on the `DiagramBuilderClientRpc` class and its
implementation on the connector `DiagramBuilderConnector`.

#### Example Java action to JoinJS

Let's imagine that I want to implement a feature "Export to PNG" that is a feature that is available on JoinJS, but I
want that it is executed when I click on a Vaadin button. According to the structure explained before, I need to:

On `src/main/webapp/VAADIN/src/main.js` define the function `openAsPNG`. Here I will implement all JoinJS featured of
how to export to PNG as provided by JoinJS documentation:

```
openAsPNG : function() {
				var windowFeatures = 'menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes';
				var windowName = _.uniqueId('png_output');
				var imageWindow = window.open('', windowName, windowFeatures);

				this.paper.toPNG(function(dataURL) {
					imageWindow.document.write('<img src="' + dataURL + '"/>');
				}, {
					padding : 10
				});
			},
```

Now, I need to create a connector to link this action to my application. For this purpose, I
update `src/main/java/com/biit/jointjs/diagram/builder/client/ui/VDiagramBuider.java` and I add a Java
function `openAsPng()` that simply uses the GWT code that forces to execute the JavaScript function just defined on
the `main.js` function.

```
public native void openAsPng()
/*-{
	$wnd.app.openAsPNG();
}-*/
;
```

Later we need to prepare the ClientRPC to execute the `openAsPng()` method. We update the
Interface `com.biit.jointjs.diagram.builder.client.DiagramBuilderClientRpc` and adds the implementation on
the `DiagramBuilderServerRpcImpl` inside the `com.biit.jointjs.diagram.builder.client.DiagramBuilderConnector` class.
Here we are telling the widget to execute the GWT code when using this method.

```
public void openAsPng() {
	getWidget().openAsPng();
}
```

The last thing is to update the `com.biit.jointjs.diagram.builder.server.DiagramBuilder.java` class that is the real
Vaadin Component that we will use on other applications. The methods here will be the one that will be called from
Vaadin on the UX.

```
public void openAsPng() {
	getClientRpc().openAsPng();
}
```

### Create a connector to link JoinJS actions to Java

The idea here is to define a callback on javascript that will generate an event that will be registered by a Java
listener.

First we need to define the callback action on `src/main/webapp/VAADIN/src/main.js` based on JoinJS library. Depending
on the JoinJS action will be easier or harder.

Later we need to register the callback on `src/main/java/com/biit/jointjs/diagram/builder/client/ui/VDiagramBuider.java`
using GWT as before. GWT will execute a Java function when receiving the event from JoinJS.

As we want a component, the best way is to use the observable pattern. That means that we will launch a custom Java
based event when a GWT event is received. This event can be consumed by any listener as is standard on a Java
Application.

#### Example JoinJS action to Java

If some actions are handle by JoinJS, they must be sent to Java as the backend probably must react due to the action. We
can add or remove a JoinJS node that must be at the end stored or removed from the database. Let's put as an example the
action to add an element on the diagram.

On `src/main/webapp/VAADIN/src/main.js`, on the `initializeUpdateCallbacks` we handle the callbacks provided by JoinJs
and link them to a custom handler that we will define on the next actions:

```
initializeUpdateCallbacks : function() {
  this.graph.on('add', function(cell) {
    fireAddElementHandler(JSON.stringify(cell.toJSON()));
  });
...
}
```

On this example, we pass all node information as JSON. This information will be later stored on database as a Java
entity.

Now on the `src/main/java/com/biit/jointjs/diagram/builder/client/ui/VDiagramBuider.java` we define what does
the `fireAddElementHandler` method on GWT, and we link it to a Java method.

Here we define the GWT method:

```
$wnd.fireAddElementHandler = function(jsonString) {
	if($wnd.enabledListeners){
		$wnd.javaInstance.@com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder::fireAddElementHandler(Ljava/lang/String;)(jsonString);
  }
};
```

And later on the same class, we have the function defined on Java:

```
protected void fireAddElementHandler(String jsonString) {
	fireEvent(new AddElementEvent(jsonString));
}
```

Now the event is launched on Java, and we need to implement what we want to do with this action. Still on
the `VDiagramBuider.java` we register the handler:

```
public HandlerRegistration addAddElementHandler(AddElementHandler handler) {
	return addHandler(handler, AddElementEvent.TYPE);
}
```

We use this handler on the `com.biit.jointjs.diagram.builder.client.DiagramBuilderConnector` class to define some
actions. On this example we want that the just added item has the focus, and the backend does something with it:

```
getWidget().addAddElementHandler(new AddElementHandler() {
	public void addElement(AddElementEvent event) {
		if (isEnabled()) {
			serverRpc.getFocus();
			serverRpc.addElement(event.getJsonString());
		} else {
			getWidget().clear();
		}
	}
});
```

The most interesting part is the `serverRpc.addElement(event.getJsonString());` line, where we get the GWT event, and
call the server RPC to perform an action. As are still events, we need to implement on the `DiagramBuilderServerRpc`
what we do on this event:

```
public void addElement(String jsonString) {
	fireElementActionAddListeners(jsonString);
}
```

That is basically to link the event to one or more listeners. As usual on the observable pattern, any object that is
registered as a listener will get a notification. On this project, we
have `com.biit.jointjs.diagram.builder.server.listeners.ElementActionListener`
that includes the action for adding an element. Then we use it to fire the listener:

```
protected void fireElementActionAddListeners(String jsonString){
	for (ElementActionListener listener : elementActionListeners) {
		listener.addElement(jsonString);
	}
}
```

Now we need to register to this listener to perform an action when we receive the event. This is done on
the `com.biit.jointjs.diagram.builder.server.DiagramBuilder.java`:

```
public void addElementActionListener(ElementActionListener listener) {
	elementActionListeners.add(listener);
}
```

Here your Vaadin UX application will register and perform the action required. For example, on the `demoproject` we use
it to simply show a message on console, but a real application must get the information and store it on the database as
example.

## How to compile

If you have installed Maven, it will do all hard work for you. Simply execute on the root folder of the project:

```
mvn clean install
```

For compiling currently a Vaadin 7 license is needed.

### How to test

If the project is compiled successfully, you can test it as a standalone application. It has already an embedded server
that avoid that you need to install any complex stuff. Simply execute on the root folder of the project:

```
mvn jetty:run
```

And open a browser and access to the URL `http://localhost:8081/`. Port 8081 is the default for this project and is
defined on the `pom.xml` file.

