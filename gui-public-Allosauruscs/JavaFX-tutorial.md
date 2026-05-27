# JavaFX Tutorial
The following excerpt from [jenkov.com JavaFX Tutorial](https://jenkov.com/tutorials/javafx/overview.html) will help you get started with understanding JavaFX and its various components

## Overview
In general, a JavaFX application contains one or more stages which corresponds to windows. Each stage has a scene attached to it. Each scene can have an object graph of controls, layouts etc. attached to it, called the scene graph. These concepts are all explained in more detail later. Here is an illustration of the general structure of a JavaFX application:

![Illustration of the tree-like structure of a JavaFX Application](https://jenkov.com/images/java-javafx/javafx-overview-1.png)

## Stage
The _stage_ is the outer frame for a JavaFX application. The stage corresponds to a window.

A JavaFX application can have multiple windows open. Each window has its own stage.

Each stage is represented by a `Stage` object inside a JavaFX application. A JavaFX application has a primary `Stage` object which is created for you by the JavaFX runtime. A JavaFX application can create additional `Stage` objects if it needs additional windows open. For instance, for dialogs, wizards etc.

## Scene
To display anything on a stage in a JavaFX application, you need a scene. A stage can only show one scene at a time, but it is possible to exchange the scene at runtime. Just like a stage in a theater can be rearranged to show multiple scenes during a play, a stage object in JavaFX can show multiple scenes (one at a time) during the life time of a JavaFX application.

When the application needs to change from one screen to the next, it simply attaches the corresponding scene to the `Stage` object of the JavaFX application.

A scene is represented by a `Scene` object inside a JavaFX application. A JavaFX application must create all `Scene` objects it needs.

## Scene Graph
All visual components (controls, layouts etc.) must be attached to a scene to be displayed, and that scene must be attached to a stage for the whole scene to be visible. The total object graph of all the controls, layouts etc. attached to a scene is called the _scene graph_.

## Nodes
All components attached to the scene graph are called nodes. All nodes are subclasses of a JavaFX class called `javafx.scene.Node`.

There are two types of nodes: Branch nodes and leaf nodes. A branch node is a node that can contain other nodes (child nodes). Branch nodes are also referred to as parent nodes because they can contain child nodes. A leaf node is a node which cannot contain other nodes.

## Controls
JavaFX controls are JavaFX components which provide some kind of control functionality inside a JavaFX application. For instance, a button, radio button, table, tree etc.

For a control to be visible it must be attached to the scene graph of some Scene object.

Controls are usually nested inside some JavaFX layout component that manages the layout of controls relative to each other.

JavaFX contains the following controls:

- Accordion
- Button
- CheckBox
- ChoiceBox
- ColorPicker
- ComboBox
- DatePicker
- Label
- ListView
- Menu
- MenuBar
- PasswordField
- ProgressBar
- RadioButton
- Slider
- Spinner
- SplitMenuButton
- SplitPane
- TableView
- TabPane
- TextArea
- TextField
- TitledPane
- ToggleButton
- ToolBar
- TreeTableView
- TreeView

## Layouts
JavaFX layouts are components which contains other components inside them. The layout component manages the layout of the components nested inside it. JavaFX layout components are also sometimes called parent components because they contain child components, and because layout components are subclasses of the JavaFX class `javafx.scene.Parent`.

A layout component must be attached to the scene graph of some `Scene` object to be visible.

JavaFX contains the following layout components:

- Group
- Region
- Pane
- HBox
- VBox
- FlowPane
- BorderPane
- BorderPane
- StackPane
- TilePane
- GridPane
- AnchorPane
- TextFlow

## FXML
JavaFX FXML is an XML format that enables you to compose JavaFX GUIs in a fashion similar to how you compose web GUIs in HTML. FXML thus enables you to separate your JavaFX layout code from the rest of your application code. This cleans up both the layout code and the rest of the application code.